<template>
    <div>
        <div class="user-search-container">
            <el-input
                    class="user-search-name"
                    placeholder="Enter name search"
                    prefix-icon="el-icon-search"
                    v-model="searchData.name"
                    @keydown.enter.native="handleSearch">
            </el-input>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">search for</el-button>
        </div>
        <div
                class="user-container"
                v-loading="loading"
                element-loading-text="loading..."
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(0, 0, 0, 0.8)">
            <el-card class="user-card" v-for="(user, index) in users" :key="index">
                <div slot="header">
                    <span>{{user.name}}</span>
                    <el-button class="user-card-delete-btn" size="medium" type="text" icon="el-icon-delete"
                               @click="handleDelete(user)"></el-button>
                </div>
                <div>
                    <div class="user-card-userface-container">
                        <img class="user-card-userface" :src="user.userface" :alt="user.name" :title="user.name">
                    </div>
                    <div class="user-card-userinfo-container">
                        <div>username：{{user.username}}</div>
                        <div>mobile phone number：{{user.phone}}</div>
                        <div>telephone number：{{user.telephone}}</div>
                        <div>address：{{user.address}}</div>
                        <div>Whether to enable：
                            <el-switch
                                    v-model="user.enabled"
                                    active-color="#13ce66"
                                    inactive-color="#ff4949"
                                    active-text="Enable"
                                    inactive-text="Disable"
                                    @change="handleEditEnabled(user)">
                            </el-switch>
                        </div>
                        <div>User role：
                            <el-tag class="user-card-userinfo-role" type="success" v-for="(role, index2) in user.roles"
                                    :key="index2">{{role.nameZh}}
                            </el-tag>
                            <el-popover
                                    placement="right"
                                    title="Role list"
                                    width="200"
                                    trigger="click"
                                    @show="handleShow(user)"
                                    @hide="handleHide(user)">
                                <el-select v-model="selectedRoleIds" multiple placeholder="please choose">
                                    <el-option
                                            v-for="(r, index3) in roles"
                                            :key="index3"
                                            :label="r.nameZh"
                                            :value="r.id">
                                    </el-option>
                                </el-select>
                                <el-button slot="reference" icon="el-icon-more" type="text"></el-button>
                            </el-popover>
                        </div>
                        <div>Remarks：{{user.remark}}</div>
                    </div>
                </div>
            </el-card>
        </div>
    </div>
</template>

<script>
    export default {
        name: "SysUser",
        data() {
            return {
                searchData: {
                    name: ''
                },
                users: [],
                roles: [],
                selectedRoleIds: [],
                loading: false
            }
        },
        mounted() {
            this.initUsers();
        },
        methods: {
            initUsers() {
                this.loading = true;
                this.getRequest("/system/user/getListWithRoles?name=" + this.searchData.name).then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.users = resp;
                    }
                });
            },
            initRoles() {
                this.getRequest("/system/basic/role/getListAll").then(resp => {
                    if (resp) {
                        this.roles = resp;
                    }
                });
            },
            handleSearch() {
                this.initUsers();
            },
            handleEditEnabled(user) {
                delete user.roles; // There is a problem with the list mapping when requesting，So delete here first
                this.putRequest("/system/user/edit", user).then(resp => {
                    if (resp) {
                        this.initUsers();
                    }
                });
            },
            handleDelete(user) {
                this.$confirm('This operation will be permanently deleted【' + user.name + '】user, Whether to continue?', 'prompt', {
                    confirmButtonText: 'determine',
                    cancelButtonText: 'cancel',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/system/user/deleteById/" + user.id).then(resp => {
                        if (resp) {
                            this.initUsers();
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: 'Undeleted'
                    });
                });
            },
            handleShow(user) {
                this.initRoles();
                let roles = user.roles;
                this.selectedRoleIds = [];
                roles.forEach(role => {
                    this.selectedRoleIds.push(role.id);
                })
            },
            handleHide(user) {
                let uid = user.id;
                if (!uid) {
                    this.$message.error('User cannot be empty');
                    return;
                }

                let flag = false;// Do you need to update
                let roles = [];
                Object.assign(roles, user.roles);// A copy of the data
                if (roles.length != this.selectedRoleIds.length) {
                    flag = true;
                } else {
                    for (let i = 0; i < roles.length; i++) {
                        let role = roles[i];
                        for (let j = 0; j < this.selectedRoleIds.length; j++) {
                            let sr = this.selectedRoleIds[j];
                            if (role.id == sr) {
                                roles.splice(i, 1);// Remove duplicate
                                i--;
                                break;
                            }
                        }
                    }
                    if (roles.length != 0) {
                        flag = true;
                    }
                }

                if (flag) {
                    let url = "/system/user/editUserRole?uid=" + uid;
                    this.selectedRoleIds.forEach(rid => {
                        url += "&rids=" + rid;
                    });
                    this.putRequest(url).then(resp => {
                        if (resp) {
                            this.initUsers();
                        }
                    });
                }
            }
        }
    }
</script>

<style scoped>
    .user-search-container {
        display: flex;
        justify-content: center;
    }

    .user-search-name {
        width: 30%;
        padding-right: 10px;
    }

    .user-container {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-around;
        margin-top: 10px;
    }

    .user-card {
        width: 30%;
        margin-top: 10px;
    }

    .user-card-delete-btn {
        float: right;
        padding: 3px 0;
        color: red;
    }

    .user-card-userface-container {
        width: 100%;
        display: flex;
        justify-content: center;
    }

    .user-card-userface {
        width: 72px;
        height: 72px;
        border-radius: 72px;
    }

    .user-card-userinfo-container {
        margin-top: 10px;
    }

    .user-card-userinfo-container > div {
        font-size: 14px;
        color: #409eff;
        margin-top: 5px;
    }

    .user-card-userinfo-role {
        margin-right: 5px;
    }
</style>
