<template>
    <div>
        <div class="salary-toolbar-container">
            <el-button type="primary" icon="el-icon-plus" @click="handleAdd">New salary account set</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">Refresh</el-button>
        </div>
        <div>
            <el-table
                    class="salary-table"
                    v-loading="loading"
                    element-loading-text="loading..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    :data="salaries"
                    stripe
                    border
                    @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="name" label="A / C set name" width="120"></el-table-column>
                <el-table-column prop="basicSalary" label="Basic wage" width="80"></el-table-column>
                <el-table-column prop="trafficSalary" label="Transport Allowance" width="80"></el-table-column>
                <el-table-column prop="lunchSalary" label="Lunch allowance" width="80"></el-table-column>
                <el-table-column prop="bonus" label="bonus" width="60"></el-table-column>
                <el-table-column prop="createDate" label="Creation time" width="140"></el-table-column>
                <el-table-column label="pension" align="center">
                    <el-table-column prop="pensionPer" label="ratio" width="60"></el-table-column>
                    <el-table-column prop="pensionBase" label="Cardinality" width="60"></el-table-column>
                </el-table-column>
                <el-table-column label="medical insurance" align="center">
                    <el-table-column prop="medicalPer" label="ratio" width="60"></el-table-column>
                    <el-table-column prop="medicalBase" label="Cardinality" width="60"></el-table-column>
                </el-table-column>
                <el-table-column label="Provident Fund" align="center">
                    <el-table-column prop="accumulationFundPer" label="ratio" width="60"></el-table-column>
                    <el-table-column prop="accumulationFundBase" label="Cardinality" width="60"></el-table-column>
                </el-table-column>
                <el-table-column label="operating" align="center">
                    <template slot-scope="scope">
                        <el-button size="mini" @click="handleEdit(scope.row)">edit</el-button>
                        <el-button size="mini" type="danger" @click="handleDelete(scope.row)">delete</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-button
                    class="salary-deletemulti-btn"
                    type="danger"
                    :disabled="this.multipleSelection.length == 0"
                    @click="handleDeleteMulti">batch deletion
            </el-button>
        </div>
        <el-dialog
                :title="dialogTitle"
                :visible.sync="dialogVisible"
                width="50%">
            <div class="salary-steps-container">
                <el-steps direction="vertical" :active="activeStepIndex">
                    <el-step :title="name" v-for="(name,index) in salaryLabel" :key="index"></el-step>
                </el-steps>
                <el-input v-model="salary[title]" :placeholder="'please enter'+salaryLabel[index]+'...'"
                          v-for="(value,title,index) in salary"
                          :key="index" v-show="activeStepIndex==index" style="width: 50%"></el-input>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">take Dispel</el-button>
                <el-button type="primary" v-show="btnPreVisible" @click="handlePreStep">Previous</el-button>
                <el-button type="primary" v-show="btnNextVisible" @click="handleNextStep">Next step</el-button>
                <el-button type="primary" v-show="btnConfirmVisible" @click="handleAddEditConfirm">Indeed set</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "SalSob",
        data() {
            return {
                salaries: [],
                multipleSelection: [],
                dialogTitle: '',
                dialogVisible: false,
                salary: {
                    // The order needs to be with salaryLabel correspond
                    name: '',
                    basicSalary: 0,
                    trafficSalary: 0,
                    lunchSalary: 0,
                    bonus: 0,
                    pensionPer: 0,
                    pensionBase: 0,
                    medicalPer: 0,
                    medicalBase: 0,
                    accumulationFundPer: 0,
                    accumulationFundBase: 0
                },
                salaryLabel: [
                    // The order needs to be with salary correspond
                    'A / C set name',
                    'Basic wage',
                    'Transport Allowance',
                    'Lunch allowance',
                    'bonus',
                    'Pension ratio',
                    'Pension base',
                    'Medical insurance ratio',
                    'Medical insurance base',
                    'Provident Fund Ratio',
                    'Provident Fund Base'
                ],
                activeStepIndex: 0,
                btnConfirmVisible: false,
                btnPreVisible: false,
                btnNextVisible: true,
                loading: false
            }
        },
        watch: {
            activeStepIndex(val) {
                if (val == 0) {
                    this.btnConfirmVisible = false;
                    this.btnPreVisible = false;
                    this.btnNextVisible = true;
                } else if (val == 10) {
                    this.btnConfirmVisible = true;
                    this.btnPreVisible = true;
                    this.btnNextVisible = false;
                } else {
                    this.btnConfirmVisible = false;
                    this.btnPreVisible = true;
                    this.btnNextVisible = true;
                }
            }
        },
        mounted() {
            this.initSalaries();
        },
        methods: {
            initSalaries() {
                this.loading = true;
                this.getRequest("/salary/sob/getListAll").then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.salaries = resp;
                    }
                });
            },
            initSalary() {
                this.salary = {
                    // The order needs to be with salaryLabel correspond
                    name: '',
                    basicSalary: 0,
                    trafficSalary: 0,
                    lunchSalary: 0,
                    bonus: 0,
                    pensionPer: 0,
                    pensionBase: 0,
                    medicalPer: 0,
                    medicalBase: 0,
                    accumulationFundPer: 0,
                    accumulationFundBase: 0
                };
            },
            initSalaryTest() {
                this.salary = {
                    // The order needs to be with salaryLabel correspond
                    name: 'Test payroll',
                    basicSalary: 8888,
                    trafficSalary: 500,
                    lunchSalary: 600,
                    bonus: 700,
                    pensionPer: 0.05,
                    pensionBase: 5000,
                    medicalPer: 0.06,
                    medicalBase: 6000,
                    accumulationFundPer: 0.07,
                    accumulationFundBase: 7000
                };
            },
            handleRefresh() {
                this.initSalaries();
            },
            handleAdd() {
                this.initSalary();
                // this.initSalaryTest();
                this.dialogTitle = 'New salary account set';
                this.activeStepIndex = 0;
                this.dialogVisible = true;
            },
            handleEdit(data) {
                this.initSalary();
                // Because the requirements are in order，So it can't be used Object.assign，Need to set manually
                // Object.assign(this.salary, data);// A copy of the data
                this.salary.name = data.name;
                this.salary.basicSalary = data.basicSalary;
                this.salary.trafficSalary = data.trafficSalary;
                this.salary.lunchSalary = data.lunchSalary;
                this.salary.bonus = data.bonus;
                this.salary.pensionPer = data.pensionPer;
                this.salary.pensionBase = data.pensionBase;
                this.salary.medicalPer = data.medicalPer;
                this.salary.medicalBase = data.medicalBase;
                this.salary.accumulationFundPer = data.accumulationFundPer;
                this.salary.accumulationFundBase = data.accumulationFundBase;
                this.salary.id = data.id;
                this.dialogTitle = 'Edit Pay Set';
                this.activeStepIndex = 0;
                this.dialogVisible = true;
            },
            handlePreStep() {
                if (this.activeStepIndex != 0) {
                    this.activeStepIndex--;
                }
            },
            handleNextStep() {
                if (this.activeStepIndex != 10) {
                    this.activeStepIndex++;
                }
            },
            handleAddEditConfirm() {
                // TODO No form verification
                if (this.salary.id) {
                    this.putRequest("/salary/sob/edit", this.salary).then(resp => {
                        if (resp) {
                            this.dialogVisible = false;
                            this.initSalaries();
                        }
                    });
                } else {
                    this.postRequest("/salary/sob/add", this.salary).then(resp => {
                        if (resp) {
                            this.dialogVisible = false;
                            this.initSalaries();
                        }
                    });
                }
            },
            handleDelete(data) {
                this.$confirm('This operation will be permanently deleted【' + data.name + '】Payroll, Whether to continue?', 'prompt', {
                    confirmButtonText: 'determine',
                    cancelButtonText: 'cancel',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/salary/sob/deleteById/" + data.id).then(resp => {
                        if (resp) {
                            this.initSalaries();
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
                    this.deleteRequest("/salary/sob/deleteByIds?" + ids).then(resp => {
                        if (resp) {
                            this.initSalaries();
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
            },
        }
    }
</script>

<style scoped>
    .salary-toolbar-container {
        display: flex;
        justify-content: space-between;
    }

    .salary-table {
        width: 100%;
        margin-top: 10px;
    }

    .salary-deletemulti-btn {
        margin-top: 10px;
    }

    .salary-steps-container {
        display: flex;
        justify-content: space-around;
        align-items: center;
    }
</style>
