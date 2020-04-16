import Vue from 'vue'
import Vuex from 'vuex'
import {Notification} from 'element-ui';
import {getRequest} from "../utils/api";
// import '../utils/sockjs';
// import '../utils/stomp';
// import SockJS from '../utils/sockjs';
// import Stomp from '../utils/stomp';
import SockJS from 'sockjs-client'; // Need first npm install sockjs-client
import Stomp from 'stompjs'; // Need first npm install stompjs

Vue.use(Vuex);

const now = new Date();

const store = new Vuex.Store({
    state: {
        routes: [], // menu
        // The following attributes are used in the online chat module
        stomp: null,
        searchData: {
            name: ''
        }, // Search user name
        users: [], // user list
        isDot: {}, // A small red dot appears in the user list
        currentUser: JSON.parse(window.sessionStorage.getItem("user")), // Currently logged in user
        chatUser: null, // Chat with
        chatMessages: {} // Chat message
    },
    mutations: {
        // Initialize the menu（initMenu）Will be called
        initRoutes(state, data) {
            state.routes = data;
        },
        // Initialize the currently logged in user，Will be called after successful login
        initCurrentUser(state, user) {
            state.currentUser = user;
        },
        // Initialize user list data
        initUsers(state) {
            getRequest("/chat/getUserList?name=" + state.searchData.name).then(resp => {
                if (resp) {
                    state.users = resp;
                }
            });
        },
        // Initialize chat messages
        initChatMessages(state) {
            // Get locally from the browser
            let data = localStorage.getItem('hr-chat-messages');
            if (data) {
                state.chatMessages = JSON.parse(data);
            }
        },
        // Handle chat，User list will be called when selecting a user
        handleChatUser(state, chatUser) {
            // Must be used Vue.set Set value，Otherwise it will not be Vue Automatically detected
            Vue.set(state.isDot, state.currentUser.username + '#' + chatUser.username, false);
            state.chatUser = chatUser;
        },
        // Handling messages in the chat box（Currently logged in user currentUser.username Message object with chat message.to），Will be called when sending and receiving messages
        handleChatMessages(state, message) {
            // console.log("111---" + JSON.stringify(message));
            let key = state.currentUser.username + '#' + message.to;
            let mss = state.chatMessages[key];
            if (!mss) {
                // Must be used Vue.set Set value，Otherwise it will not be Vue Automatically detected
                // state.chatMessages[state.currentUser.username+'#'+message.to] = [];
                Vue.set(state.chatMessages, key, []);
            }
            state.chatMessages[key].push({
                self: message.self,
                content: message.content,
                date: message.date
            })
        }
    },
    actions: {
        // Initiating online chat WebSocket connection，And start subscribing and receiving chat messages，Initialize the menu（initMenu）Will be called
        initChatConnect(context) {
            context.state.stomp = Stomp.over(new SockJS('/ws/ep'));
            context.state.stomp.connect({}, success => {
                // by /user/queue/chat Subscribe to news，Added by default /user Prefix
                context.state.stomp.subscribe('/user/queue/chat', msg => {
                    let chatMsg = JSON.parse(msg.body);
                    if (!context.state.chatUser || chatMsg.from != context.state.chatUser.username) {
                        Notification.info({
                            title: '【' + chatMsg.fromName + '】Sent a message',
                            message: chatMsg.content.length > 10 ? chatMsg.content.substr(0, 10) : chatMsg.content,
                            position: 'bottom-right'
                        });
                        // Must be used Vue.set Set value，Otherwise it will not be Vue Automatically detected
                        Vue.set(context.state.isDot, context.state.currentUser.username + '#' + chatMsg.from, true);
                    }

                    let message = new Object();
                    message.self = false;
                    message.to = chatMsg.from; // note：message.to Refers to the chat message object in the chat box with the currently logged in user
                    message.content = chatMsg.content;
                    message.date = chatMsg.date;
                    context.commit('handleChatMessages', message);
                })
            }, error => {

            })
        },
        // Initialize data related to online chat，Will be called when opening the chat page
        initChatData(context) {
            // Initialize user list data
            context.commit('initUsers');
            // Initialize chat messages
            context.commit('initChatMessages');
        }
    }
});

// monitor state.chatMessages The change，Save to the browser locally
store.watch(function (state) {
    return state.chatMessages
}, function (val) {
    localStorage.setItem('hr-chat-messages', JSON.stringify(val));
}, {
    deep: true/*This seems to be onwatchMonitoring judgment,The official description is also vague*/
});

export default store;
