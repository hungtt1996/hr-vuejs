<template>
    <div
            v-loading="addLoading"
            element-loading-text="Adding..."
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)">
        <div>
            <el-input class="role-add-name" placeholder="Please enter a role name" v-model="role.name">
                <template slot="prepend">ROLE_</template>
            </el-input>
            <el-input class="role-add-nameZh" placeholder="Please enter the Chinese character name" v-model="role.nameZh"
                      @keydown.enter.native="handleAdd"></el-input>
            <el-button type="primary" icon="el-icon-plus" @click="handleAdd">New role</el-button>
        </div>
        <div>
            <el-collapse
                    class="role-collapse"
                    v-loading="loading"
                    element-loading-text="loading..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    v-model="activeName"
                    accordion
                    @change="handleChange">
                <el-collapse-item :title="role.nameZh" :name="role.id" v-for="(role,index) in roles" :key="index">
                    <el-card>
                        <div slot="header">
                            <span>Accessible menu</span>
                            <el-button class="role-delete-btn" size="medium" type="text" icon="el-icon-delete"
                                       @click="handleDelete(role)"></el-button>
                        </div>
                        <div>
                            <el-tree
                                    show-checkbox
                                    node-key="id"
                                    ref="tree"
                                    :key="index"
                                    :data="menus"
                                    :default-checked-keys="defaultCheckedKeys"
                                    :props="defaultProps">
                            </el-tree>
                            <div class="role-edit-div">
                                <el-button size="mini" @click="activeName = -1">take Dispel</el-button>
                                <el-button size="mini" type="primary" @click="handleEditRoleMenu(role.id, index)">Indeed set
                                </el-button>
                            </div>
                        </div>
                    </el-card>
                </el-collapse-item>
            </el-collapse>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Role",
        data() {
            return {
                role: {
                    name: '',
                    nameZh: ''
                },
                activeName: -1, // Do not expand any panel
                roles: [],
                menus: [],
                defaultCheckedKeys: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                loading: false,
                addLoading: false
            }
        },
        mounted() {
            this.initRoles();
        },
        methods: {
            initRoles() {
                this.loading = true;
                this.getRequest("/system/basic/role/getListAll").then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.roles = resp;
                    }
                });
            },
            initRole() {
                this.role = {
                    name: '',
                    nameZh: ''
                };
            },
            initMenus() {
                this.getRequest("/system/basic/role/getMenuListAllWithChildren").then(resp => {
                    if (resp) {
                        this.menus = resp;
                    }
                });
            },
            initCheckedKeys(rid) {
                this.getRequest("/system/basic/role/getMidsByRid/" + rid).then(resp => {
                    if (resp) {
                        this.defaultCheckedKeys = resp;
                    }
                });
            },
            handleAdd() {
                if (this.role.name && this.role.nameZh) {
                    this.addLoading = true;
                    this.postRequest("/system/basic/role/add", this.role).then(resp => {
                        this.addLoading = false;
                        if (resp) {
                            this.initRoles();
                            this.initRole();
                        }
                    });
                } else {
                    this.$message.error('Role name and role Chinese name cannot be empty');
                }
            },
            handleEditRoleMenu(rid, index) {
                if (rid) {
                    let tree = this.$refs.tree[index];
                    let checkedKeys = tree.getCheckedKeys(tree);
                    let url = "/system/basic/role/editRoleMenu?rid=" + rid;
                    checkedKeys.forEach(key => {
                        url += "&mids=" + key;
                    });
                    this.putRequest(url).then(resp => {
                        if (resp) {
                            this.activeName = -1;
                        }
                    });
                } else {
                    this.$message.error('Role cannot be empty');
                }
            },
            handleDelete(role) {
                this.$confirm('This operation will be permanently deleted【' + role.nameZh + '】Roles, Whether to continue?', 'prompt', {
                    confirmButtonText: 'determine',
                    cancelButtonText: 'cancel',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/system/basic/role/deleteById/" + role.id).then(resp => {
                        if (resp) {
                            this.initRoles();
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: 'Undeleted'
                    });
                });
            },
            handleChange(rid) {
                if (rid) {
                    //  TODO BUG:When switching between different folding panels，In the tree defaultCheckedKeys Values will be confused，Feeling cached
                    this.initMenus();
                    this.initCheckedKeys(rid);
                }

            }
        }
    }
</script>

<style scoped>
    .role-add-name {
        width: 25%;
    }

    .role-add-nameZh {
        width: 25%;
        margin-left: 10px;
        margin-right: 10px;
    }

    .role-collapse {
        width: 60%;
        margin-top: 10px;
    }

    .role-delete-btn {
        float: right;
        padding: 3px 0;
        color: red;
    }

    .role-edit-div {
        display: flex;
        justify-content: flex-end;
        margin-top: 10px;
    }
</style>
