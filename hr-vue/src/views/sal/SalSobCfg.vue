<template>
    <div>
        <div>
            <el-input
                    class="employee-search-name"
                    placeholder="Enter name search"
                    prefix-icon="el-icon-search"
                    clearable
                    @clear="handleSearch"
                    v-model="searchData.name"
                    @keydown.enter.native="handleSearch">
            </el-input>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">search for</el-button>
        </div>
        <div>
            <el-table
                    class="employee-table"
                    v-loading="loading"
                    element-loading-text="loading..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    :data="employees"
                    stripe
                    border
                    size="mini">
                <el-table-column prop="name" label="Name" width="100" fixed></el-table-column>
                <el-table-column prop="workId" label="Job number" width="100"></el-table-column>
                <el-table-column prop="gender" label="gender" width="80" align="center"></el-table-column>
                <el-table-column prop="birthday" label="date of birth" width="100"></el-table-column>
                <el-table-column prop="email" label="E-mail" width="160"></el-table-column>
                <el-table-column prop="phone" label="telephone number" width="120"></el-table-column>
                <el-table-column prop="department.name" label="Department" width="120"></el-table-column>
                <el-table-column label="Payroll" align="center">
                    <template slot-scope="scope">
                        <el-tooltip placement="right" v-if="scope.row.salary">
                            <div slot="content">
                                <table>
                                    <tr>
                                        <td>Basic wage</td>
                                        <td>{{scope.row.salary.basicSalary}}</td>
                                    </tr>
                                    <tr>
                                        <td>Transport Allowance</td>
                                        <td>{{scope.row.salary.trafficSalary}}</td>
                                    </tr>
                                    <tr>
                                        <td>Lunch allowance</td>
                                        <td>{{scope.row.salary.lunchSalary}}</td>
                                    </tr>
                                    <tr>
                                        <td>bonus</td>
                                        <td>{{scope.row.salary.bonus}}</td>
                                    </tr>
                                    <tr>
                                        <td>Pension ratio</td>
                                        <td>{{scope.row.salary.pensionPer}}</td>
                                    </tr>
                                    <tr>
                                        <td>Pension base</td>
                                        <td>{{scope.row.salary.pensionBase}}</td>
                                    </tr>
                                    <tr>
                                        <td>Medical insurance ratio</td>
                                        <td>{{scope.row.salary.medicalPer}}</td>
                                    </tr>
                                    <tr>
                                        <td>Medical insurance base</td>
                                        <td>{{scope.row.salary.medicalBase}}</td>
                                    </tr>
                                    <tr>
                                        <td>Provident Fund Ratio</td>
                                        <td>{{scope.row.salary.accumulationFundPer}}</td>
                                    </tr>
                                    <tr>
                                        <td>Provident Fund Base</td>
                                        <td>{{scope.row.salary.accumulationFundBase}}</td>
                                    </tr>
                                    <tr>
                                        <td>Creation time</td>
                                        <td>{{scope.row.salary.createDate}}</td>
                                    </tr>
                                </table>
                            </div>
                            <el-tag>{{scope.row.salary.name}}</el-tag>
                        </el-tooltip>
                        <el-tag v-else>Not set yet</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="operating" align="center">
                    <template slot-scope="scope">
                        <el-popover
                                placement="left"
                                title="Edit Pay Set"
                                width="200"
                                trigger="click"
                                @show="handleShow(scope.row.salary)"
                                @hide="handleHide(scope.row.id, scope.row.salary)">
                            <el-select v-model="selectedSalaryId" size="mini" placeholder="please choose">
                                <el-option
                                        v-for="salary in salaries"
                                        :key="salary.id"
                                        :label="salary.name"
                                        :value="salary.id">
                                </el-option>
                            </el-select>
                            <el-button slot="reference">Edit Pay Set</el-button>
                        </el-popover>
                    </template>
                </el-table-column>
            </el-table>
            <div class="employee-pagination-container">
                <el-pagination
                        background
                        layout="sizes, prev, pager, next, jumper, ->, total, slot"
                        :total="total"
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "SalSobCfg",
        data() {
            return {
                searchData: {
                    name: ''
                },
                employees: [],
                page: 1,
                size: 10,
                total: 0,
                salaries: [],
                selectedSalaryId: null,
                loading: false
            }
        },
        mounted() {
            this.initEmployees();
            this.initSalaries();
        },
        methods: {
            initEmployees() {
                this.loading = true;
                let url = "/salary/sobcfg/getPage?page=" + this.page + "&size=" + this.size;
                url += "&name=" + this.searchData.name;
                this.getRequest(url).then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.employees = resp.data;
                        this.total = resp.total;
                    }
                });
            },
            initSalaries() {
                this.getRequest("/salary/sobcfg/getSalaryListAll").then(resp => {
                    if (resp) {
                        this.salaries = resp;
                    }
                });
            },
            handleSizeChange(pageSize) {
                this.size = pageSize;
                this.initEmployees();
            },
            handleCurrentChange(currentPage) {
                this.page = currentPage;
                this.initEmployees();
            },
            handleSearch() {
                this.initEmployees();
            },
            handleShow(salary) {
                this.selectedSalaryId = null;
                if (salary) {
                    this.selectedSalaryId = salary.id;
                }
            },
            handleHide(id, salary) {
                if (!id) {
                    this.$message.error('Employees cannot be empty');
                    return;
                }

                let flag = false;// Do you need to update
                if (this.selectedSalaryId && (!salary || salary.id != this.selectedSalaryId)) {
                    flag = true;
                }

                if (flag) {
                    let url = "/salary/sobcfg/editSalaryId?id=" + id + "&salaryId=" + this.selectedSalaryId;
                    this.putRequest(url).then(resp => {
                        if (resp) {
                            this.initEmployees();
                        }
                    });
                }
            }
        }
    }
</script>

<style scoped>
    .employee-search-name {
        width: 30%;
        margin-right: 10px;
    }

    .employee-table {
        width: 100%;
        margin-top: 10px;
    }

    .employee-pagination-container {
        display: flex;
        justify-content: flex-end;
        margin-top: 10px;
    }
</style>
