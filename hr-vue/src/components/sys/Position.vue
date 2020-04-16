<template>
    <div>
        <div>
            <el-input
                    class="position-add-name"
                    placeholder="Please enter the job title"
                    prefix-icon="el-icon-plus"
                    v-model="positionAdd.name"
                    @keydown.enter.native="handleAdd">
            </el-input>
            <el-button type="primary" icon="el-icon-plus" @click="handleAdd">New job</el-button>
        </div>
        <div>
            <el-table
                    class="position-table"
                    v-loading="loading"
                    element-loading-text="loading..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    :data="positions"
                    stripe
                    border
                    size="mini"
                    @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="id" label="Numbering" width="60"></el-table-column>
                <el-table-column prop="name" label="Job Title" width="180"></el-table-column>
                <el-table-column prop="createDate" label="Creation time" width="180"></el-table-column>
                <el-table-column label="Whether to enable" width="100">
                    <template slot-scope="scope">
                        <el-tag type="success" v-if="scope.row.enabled">activated</el-tag>
                        <el-tag type="danger" v-else>Not activated</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="operating" align="center">
                    <template slot-scope="scope">
                        <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">edit</el-button>
                        <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">delete
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-button
                    class="position-deletemulti-btn"
                    type="danger"
                    :disabled="this.multipleSelection.length == 0"
                    @click="handleDeleteMulti">batch deletion
            </el-button>
        </div>
        <el-dialog
                title="Edit job"
                :visible.sync="dialogVisible"
                width="30%">
            <div>
                <table class="position-edit-table">
                    <tr>
                        <td>
                            <el-tag>Job Title</el-tag>
                        </td>
                        <td>
                            <el-input v-model="positionEdit.name" placeholder="Please enter the job title"></el-input>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag>Whether to enable</el-tag>
                        </td>
                        <td>
                            <el-switch
                                    v-model="positionEdit.enabled"
                                    active-text="Enable"
                                    inactive-text="Disable">
                            </el-switch>
                        </td>
                    </tr>
                </table>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">take Dispel</el-button>
                <el-button type="primary" @click="handleEditConfirm">Indeed set</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "Position",
        data() {
            return {
                positionAdd: {
                    name: ''
                },
                positionEdit: {
                    name: '',
                    enabled: false
                },
                positions: [],
                multipleSelection: [],
                dialogVisible: false,
                loading: false
            }
        },
        mounted() {
            this.initPositions();
        },
        methods: {
            initPositions() {
                this.loading = true;
                this.getRequest("/system/basic/position/getListAll").then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.positions = resp;
                    }
                });
            },
            initPositionAdd() {
                this.positionAdd = {
                    name: ''
                };
            },
            initPositionEdit() {
                this.positionEdit = {
                    name: '',
                    enabled: false
                };
            },
            handleAdd() {
                if (this.positionAdd.name) {
                    this.postRequest("/system/basic/position/add", this.positionAdd).then(resp => {
                        if (resp) {
                            this.initPositions();
                            this.initPositionAdd();
                        }
                    });
                } else {
                    this.$message.error('Job title cannot be empty');
                }
            },
            handleEdit(index, data) {
                Object.assign(this.positionEdit, data);// A copy of the data
                this.dialogVisible = true;
            },
            handleEditConfirm() {
                if (this.positionEdit.name) {
                    this.putRequest("/system/basic/position/edit", this.positionEdit).then(resp => {
                        if (resp) {
                            this.initPositions();
                            this.initPositionEdit();
                            this.dialogVisible = false;
                        }
                    });
                } else {
                    this.$message.error('Job title cannot be empty');
                }
            },
            handleDelete(index, data) {
                this.$confirm('This operation will be permanently deleted【' + data.name + '】position, Whether to continue?', 'prompt', {
                    confirmButtonText: 'determine',
                    cancelButtonText: 'cancel',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/system/basic/position/deleteById/" + data.id).then(resp => {
                        if (resp) {
                            this.initPositions();
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: 'Undeleted'
                    });
                });
            },
            handleDeleteMulti() {
                this.$confirm('This operation will be permanently deleted【' + this.multipleSelection.length + '】Records, Whether to continue?', 'prompt', {
                    confirmButtonText: 'determine',
                    cancelButtonText: 'cancel',
                    type: 'warning'
                }).then(() => {
                    let ids = "";
                    this.multipleSelection.forEach(item => {
                        if (ids != "") {
                            ids += "&";
                        }
                        ids += "ids=" + item.id;
                    });
                    this.deleteRequest("/system/basic/position/deleteByIds?" + ids).then(resp => {
                        if (resp) {
                            this.initPositions();
                        }
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: 'Undeleted'
                    });
                });
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            }
        }
    }
</script>

<style scoped>
    .position-add-name {
        width: 30%;
        margin-right: 10px;
    }

    .position-table {
        width: 80%;
        margin-top: 10px;
    }

    .position-edit-table td {
        padding: 10px 10px 0px 0px;
    }

    .position-deletemulti-btn {
        margin-top: 10px;
    }
</style>
