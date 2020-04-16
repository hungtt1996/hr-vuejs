<template>
    <div>
        <div>
            <el-input
                    class="department-search-name"
                    placeholder="Enter department name search"
                    prefix-icon="el-icon-search"
                    v-model="filterText">
            </el-input>
        </div>
        <div>
            <el-tree
                    class="department-tree"
                    v-loading="loading"
                    element-loading-text="loading..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    :data="departments"
                    :props="defaultProps"
                    :expand-on-click-node="false"
                    :filter-node-method="filterNode"
                    ref="tree">
                <span class="department-tree-node" slot-scope="{ node, data }">
                    <span>{{ data.name }}</span>
                    <span>
                        <el-button
                                class="department-tree-btn"
                                type="primary"
                                size="mini"
                                @click="handleAdd(data)">New
                            </el-button>
                        <el-button
                                class="department-tree-btn"
                                type="primary"
                                size="mini"
                                @click="handleEdit(data)">edit
                        </el-button>
                        <el-button
                                class="department-tree-btn"
                                type="danger"
                                size="mini"
                                @click="handleDelete(data)">delete
                        </el-button>
                    </span>
                  </span>
            </el-tree>
        </div>
        <el-dialog
                :title="dialogTitle"
                :visible.sync="dialogVisible"
                width="30%">
            <div>
                <table class="department-table">
                    <tr v-if="!department.id">
                        <td>
                            <el-tag>Higher office</el-tag>
                        </td>
                        <td>
                            {{this.department.parentName}}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag>Department name</el-tag>
                        </td>
                        <td>
                            <el-input v-model="department.name" placeholder="Please enter the department name"></el-input>
                        </td>
                    </tr>
                </table>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">take Dispel</el-button>
                <el-button type="primary" @click="handleAddEditConfirm">Indeed set</el-button>
              </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "Department",
        data() {
            return {
                filterText: '',
                departments: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                dialogTitle: '',
                dialogVisible: false,
                department: {
                    name: '',
                    parentId: null, // Required when adding
                    parentName: '' // Need to display when adding
                },
                loading: false
            };
        },
        watch: {
            filterText(val) {
                this.$refs.tree.filter(val);
            }
        },
        mounted() {
            this.initDepartments();
        },
        methods: {
            initDepartments() {
                this.loading = true;
                this.getRequest("/system/basic/department/getListAllWithChildren").then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.departments = resp;
                    }
                });
            },
            initDepartment() {
                this.department = {
                    name: '',
                    parentId: null,
                    parentName: ''
                };
            },
            filterNode(value, data) {
                if (!value) return true;
                return data.name.indexOf(value) !== -1;
            },
            handleTreeAdd(departments, addData) {
                // will addData Add to treeData On the corresponding parent node
                for (let i = 0; i < departments.length; i++) {
                    let data = departments[i];
                    if (data.id == addData.parentId) {
                        data.children = data.children.concat(addData);
                        if (data.children.length > 0) {
                            data.isParent = true;
                        }
                        return;
                    } else {
                        // Recursive processing children
                        this.handleTreeAdd(data.children, addData);
                    }
                }
            },
            handleTreeEdit(departments, editData) {
                // will editData Update to departments On the corresponding node
                for (let i = 0; i < departments.length; i++) {
                    let data = departments[i];
                    if (data.id == editData.id) {
                        data.name = editData.name;
                        return;
                    } else {
                        // Recursive processing children
                        this.handleTreeEdit(data.children, editData);
                    }
                }
            },
            handleTreeDelete(dataParent, departments, deleteDataId) {
                // will deleteDataId The corresponding node from departments Delete
                for (let i = 0; i < departments.length; i++) {
                    let data = departments[i];
                    if (data.id == deleteDataId) {
                        departments.splice(i, 1);
                        if (departments.length == 0) {
                            dataParent.isParent = false;
                        }
                        return;
                    } else {
                        this.handleTreeDelete(data, data.children, deleteDataId);
                    }
                }
            },
            handleAdd(data) {
                this.initDepartment();
                this.department.parentId = data.id;
                this.department.parentName = data.name;
                this.dialogTitle = 'New department';
                this.dialogVisible = true;
            },
            handleEdit(data) {
                this.initDepartment();
                Object.assign(this.department, data);// A copy of the data
                this.dialogTitle = 'Editorial department';
                this.dialogVisible = true;
            },
            handleAddEditConfirm() {
                if (this.department.name) {
                    if (this.department.id) {
                        this.putRequest("/system/basic/department/edit", this.department).then(resp => {
                            if (resp) {
                                // Only process the currently edited data，Don't initialize the whole departments
                                this.handleTreeEdit(this.departments, this.department);
                                this.dialogVisible = false;
                            }
                        });
                    } else {
                        if (this.department.parentId) {
                            this.postRequest("/system/basic/department/add", this.department).then(resp => {
                                if (resp) {
                                    // Only process the newly added data，Don't initialize the whole departments
                                    this.handleTreeAdd(this.departments, resp.obj);
                                    this.dialogVisible = false;
                                }
                            });
                        } else {
                            this.$message.error('The superior department cannot be empty');
                        }
                    }
                } else {
                    this.$message.error('Department name cannot be empty');
                }
            },
            handleDelete(data) {
                if (data.isParent) {
                    this.$message.error("failed to delete，Parent department cannot be deleted");
                } else {
                    this.$confirm('This operation will be permanently deleted【' + data.name + '】department, Whether to continue?', 'prompt', {
                        confirmButtonText: 'determine',
                        cancelButtonText: 'cancel',
                        type: 'warning'
                    }).then(() => {
                        this.deleteRequest("/system/basic/department/deleteById/" + data.id).then(resp => {
                            if (resp) {
                                // Only process the currently deleted data，Don't initialize the whole departments
                                this.handleTreeDelete(null, this.departments, data.id);
                            }
                        });
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: 'Undeleted'
                        });
                    });
                }
            }
        }
    }
</script>

<style scoped>
    .department-search-name {
        width: 30%;
    }

    .department-tree {
        width: 60%;
        margin-top: 10px;
    }

    .department-tree-node {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 14px;
        padding-right: 8px;
    }

    .department-tree-btn {
        padding: 2px 10px;
    }

    .department-table td {
        padding: 10px 10px 0px 0px;
    }
</style>
