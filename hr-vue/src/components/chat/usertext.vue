<template>
    <div id="uesrtext">
        <textarea placeholder="press Ctrl + Enter send" v-model="content"
                  v-on:keyup.ctrl.enter="handleChatMessages"></textarea>
    </div>
</template>

<script>
    import {mapState} from 'vuex'

    export default {
        name: 'uesrtext',
        data() {
            return {
                content: ''
            }
        },
        computed: mapState([
            'chatUser',
            'chatMessages'
        ]),
        methods: {
            handleChatMessages() {
                if (!this.chatUser) {
                    this.$message.error('Please select a chat first');
                    return;
                }
                if (!this.content || this.content.trim().length == 0) {
                    this.$message.error('Cant send blank message');
                    return;
                }

                let message = new Object();
                message.self = true;
                message.to = this.chatUser.username;
                message.content = this.content;
                message.date = new Date();
                // by /ws/chat Send chat messages to the background
                this.$store.state.stomp.send('/ws/chat', {}, JSON.stringify(message));
                // The front desk processes messages in the chat box
                this.$store.commit('handleChatMessages', message);
                this.content = '';
            }
        }
    }
</script>

<style lang="scss" scoped>
    #uesrtext {
        position: absolute;
        bottom: 0;
        right: 0;
        width: 100%;
        height: 30%;
        border-top: solid 1px #DDD;

        > textarea {
            padding: 10px;
            width: 100%;
            height: 100%;
            border: none;
            outline: none;
            resize: none;
        }
    }
</style>
