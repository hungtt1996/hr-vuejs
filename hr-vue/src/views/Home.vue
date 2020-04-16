<template>
    <div>
        <el-container>
            <el-header class="home-header">
                <div class="home-header-title">HR Management System</div>
                <div>
                    <el-button class="home-header-chat" type="text" icon="el-icon-message" size="normal" @click="handleChat"></el-button>
                    <el-dropdown class="home-header-user" @command="commandHandler">
                        <span class="home-header-user-name">
                            {{user.name}}<i><img :src="user.userface" alt=""></i>
                        </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="userinfo">Personal center</el-dropdown-item>
                            <el-dropdown-item command="setting">Set up</el-dropdown-item>
                            <el-dropdown-item command="logout" divided>Logout</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
            </el-header>
            <el-container>
                <el-aside width="200px">
                    <el-menu router unique-opened>
                        <el-submenu :index="index+''" v-for="(item,index) in routes" :key="index" v-if="!item.hidden">
                            <template slot="title">
                                <!--<i class="el-icon-location"></i>-->
                                <i class="home-aside-menu-icon" :class="item.iconCls"></i>
                                <span>{{item.name}}</span>
                            </template>
                            <el-menu-item :index="child.path" v-for="(child,indexj) in item.children" :key="indexj">
                                {{child.name}}
                            </el-menu-item>
                        </el-submenu>
                    </el-menu>
                </el-aside>
                <el-main>
                    <el-breadcrumb separator-class="el-icon-arrow-right" v-if="this.$router.currentRoute.path!='/home'">
                        <el-breadcrumb-item :to="{ path: '/home' }">Home</el-breadcrumb-item>
                        <el-breadcrumb-item>{{this.$router.currentRoute.name}}</el-breadcrumb-item>
                    </el-breadcrumb>
                    <div class="home-main-welcome" v-if="this.$router.currentRoute.path=='/home'">
                        Welcome to HR Management System
                    </div>
                    <router-view class="home-main-router-view"/>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "Home",
        data() {
            return {
                user: JSON.parse(window.sessionStorage.getItem("user"))
                // ,routes: this.$router.options.routes // the way1：The menu is written inrouter/index.jsofrouterin
            }
        },
        computed: {
            routes() {
                return this.$store.state.routes; // the way2：The menu is dynamically obtained from the background，Initialize the menu（initMenu）Writestore/index.jsofstorein
            }
        },
        methods: {
            handleChat() {
                this.$router.push("/chat");
            },
            commandHandler(command) {
                if (command == 'logout') {
                    this.$confirm('This operation will log out, Whether to continue?', 'prompt', {
                        confirmButtonText: 'determine',
                        cancelButtonText: 'cancel',
                        type: 'warning'
                    }).then(() => {
                        this.getRequest("/logout");
                        window.sessionStorage.removeItem("user");
                        this.$store.commit('initRoutes', []);
                        this.$router.replace("/");
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: 'Operation cancelled'
                        });
                    });
                }
            }
        }
    }
</script>

<style scoped>
    .home-header {
        background-color: #409eff;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0px 15px;
        box-sizing: border-box;
    }

    .home-header-title {
        font-size: 30px;
        font-family: Chinese Xingkai;
        color: #ffffff;
    }

    .home-header-chat{
        margin-right: 10px;
        color: #ffffff;
    }

    .home-header-user {
        cursor: pointer;
    }

    .home-header-user-name {
        display: flex;
        align-items: center;
        color: #ffffff;
    }

    .home-header-user-name img {
        width: 48px;
        height: 48px;
        border-radius: 24px;
        margin-left: 8px;
    }

    /*.home-aside {
        background-color: #ECECEC;
    }*/

    /*.home-aside-menu {
        display: flex;
        justify-content: flex-start;
        width: 200px;
        text-align: left;
        background: #ececec;
    }*/

    .home-aside-menu-icon {
        color: #409eff;
        margin-right: 5px;
    }

    /*.home-aside-menu-item {
        width: 200px;
        padding-left: 30px;
        padding-right: 0px;
        margin-left: 0px;
        text-align: left
    }*/

    .home-main-welcome {
        text-align: center;
        font-size: 30px;
        font-family: Chinese Xingkai;
        color: #409eff;
        padding-top: 50px;
    }

    .home-main-router-view {
        margin-top: 10px;
    }
</style>
