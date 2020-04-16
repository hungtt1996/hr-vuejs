<template>
    <div>
        <div>
            <el-input
                    class="jobtitle-add-name"
                    placeholder="Please enter the title"
                    prefix-icon="el-icon-plus"
                    v-model="jobTitleAdd.name"
                    @keydown.enter.native="handleAdd">
            </el-input>
            <el-select class="jobtitle-add-level" v-model="jobTitleAdd.level" placeholder="Please select the title level">
                <el-option
                        v-for="item in levels"
                        :key="item"
                        :label="item"
                        :value="item">
                </el-option>
            </el-select>
            <el-button type="primary" icon="el-icon-plus" @click="handleAdd">New job title</el-button>
        </div>
        <div>
            <el-table
                    class="jobtitle-table"
                    v-loading="loading"
                    element-loading-text="loading..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    :data="jobTitles"
                    stripe
                    border
                    size="mini"
                    @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="id" label="Numbering" width="60"></el-table-column>
                <el-table-column prop="name" label="Job title" width="180"></el-table-column>
                <el-table-column prop="level" label="Title level" width="100"></el-table-column>
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
                    class="jobtitle-deletemulti-btn"
                    type="danger"
                    :disabled="this.multipleSelection.length == 0"
                    @click="handleDeleteMulti">batch deletion
            </el-button>
        </div>
        <el-dialog
                title="Editor title"
                :visible.sync="dialogVisible"
                width="30%">
            <div>
                <table class="jobtitle-edit-table">
                    <tr>
                        <td>
                            <el-tag>Job title</el-tag>
                        </td>
                        <td>
                            <el-input v-model="jobTitleEdit.name" placeholder="Please enter the title"></el-input>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag>Title level</el-tag>
                        </td>
                        <td>
                            <el-select v-model="jobTitleEdit.level" placeholder="Please select the title level">
                                <el-option
                                        v-for="item in levels"
                                        :key="item"
                                        :label="item"
                                        :value="item">
                                </el-option>
                            </el-select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag>Whether to enable</el-tag>
                        </td>
                        <td>
                            <el-switch
                                    v-model="jobTitleEdit.enabled"
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
        name: "JobTitle",
        data() {
            return {
                levels: [
                    'Positive Senior',
                    'Deputy Senior',
                    'intermediate',
                    'primary',
                    'Staff',
                ],
                jobTitleAdd: {
                    name: '',
                    level: ''
                },
                jobTitleEdit: {
                    name: '',
                    level: '',
                    enabled: false
                },
                jobTitles: [],
                multipleSelection: [],
                dialogVisible: false,
                loading: false
            }
        },
        mounted() {
            this.initJobTitles();
        },
        methods: {
            initJobTitles() {
                this.loading = true;
                this.getRequest("/system/basic/jobTitle/getListAll").then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.jobTitles = resp;
                    }
                });
            },
            initJobTitleAdd() {
                this.jobTitleAdd = {
                    name: '',
                    level: ''
                };
            },
            initJobTitleEdit() {
                this.jobTitleEdit = {
                    name: '',
                    level: '',
                    enabled: false
                };
            },
            handleAdd() {
                if (this.jobTitleAdd.name && this.jobTitleAdd.level) {
                    this.postRequest("/system/basic/jobTitle/add", this.jobTitleAdd).then(resp => {
                        if (resp) {
                            this.initJobTitles();
                            this.initJobTitleAdd();
                        }
                    });
                } else {
                    this.$message.error('Title and title level cannot be empty');
                }
            },
            handleEdit(index, data) {
                Object.assign(this.jobTitleEdit, data);// A copy of the data
                this.dialogVisible = true;
            },
            handleEditConfirm() {
                if (this.jobTitleEdit.name && this.jobTitleEdit.level) {
                    this.putRequest("/system/basic/jobTitle/edit", this.jobTitleEdit).then(resp => {
                        if (resp) {
                            this.initJobTitles();
                            this.initJobTitleEdit();
                            this.dialogVisible = false;
                        }
                    });
                } else {
                    this.$message.error('Title and title level cannot be empty');
                }
            },
            handleDelete(index, data) {
                this.$confirm('This operation will be permanently deleted【' + data.name + '】job title, Whether to continue?', 'prompt', {
                    confirmButtonText: 'determine',
                    cancelButtonText: 'cancel',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/system/basic/jobTitle/deleteById/" + data.id).then(resp => {
                        if (resp) {
                            this.initJobTitles();
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
                    this.deleteRequest("/system/basic/jobTitle/deleteByIds?" + ids).then(resp => {
                        if (resp) {
                            this.initJobTitles();
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
    .jobtitle-add-name {
        width: 30%;
    }

    .jobtitle-add-level {
        margin-left: 10px;
        margin-right: 10px;
    }

    .jobtitle-table {
        width: 80%;
        margin-top: 10px;
    }

    .jobtitle-edit-table td {
        padding: 10px 10px 0px 0px;
    }

    .jobtitle-deletemulti-btn {
        margin-top: 10px;
    }
</style>
