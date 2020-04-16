<template>
    <div>
        <div>
            <div class="employee-toolbar-container">
                <div>
                    <el-input
                            class="employee-search-name"
                            placeholder="Enter name search"
                            prefix-icon="el-icon-search"
                            clearable
                            @clear="handleSearch"
                            :disabled="searchAdvViewVisible"
                            v-model="searchData.name"
                            @keydown.enter.native="handleSearch">
                    </el-input>
                    <el-button type="primary" icon="el-icon-search" :disabled="searchAdvViewVisible"
                               @click="handleSearch">
                        search for
                    </el-button>
                    <el-button type="primary" icon="el-icon-search" @click="handleSearchAdv">
                        <i :class="searchAdvViewVisible?'fa fa-angle-double-up':'fa fa-angle-double-down'"
                           aria-hidden="true"></i>
                        Advanced Search
                    </el-button>
                </div>
                <div>
                    <el-upload
                            :show-file-list="false"
                            :before-upload="handleBeforeUpload"
                            :on-success="handleSuccess"
                            :on-error="handleError"
                            :disabled="importBtnDisabled"
                            style="display: inline-flex;margin-right: 8px"
                            action="/employee/basic/import">
                        <el-button :disabled="importBtnDisabled" type="success" :icon="importBtnIcon">
                            {{importBtnText}}
                        </el-button>
                    </el-upload>
                    <el-button type="success" icon="el-icon-upload2" @click="handleExport">Export</el-button>
                    <el-button type="primary" icon="el-icon-plus" @click="handleAdd">New employee</el-button>
                </div>
            </div>
            <transition name="slide-fade">
                <div class="employee-searchadv" v-show="searchAdvViewVisible">
                    <el-row>
                        <el-col :span="5">
                            political status:
                            <el-select v-model="searchAdvData.politicsStatusId" placeholder="political status" size="mini"
                                       style="width: 130px;">
                                <el-option
                                        v-for="item in politicsStatuses"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="4">
                            Nation:
                            <el-select v-model="searchAdvData.nationId" placeholder="Nation" size="mini"
                                       style="width: 130px;">
                                <el-option
                                        v-for="item in nations"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="4">
                            position:
                            <el-select v-model="searchAdvData.positionId" placeholder="position" size="mini"
                                       style="width: 130px;">
                                <el-option
                                        v-for="item in positions"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="4">
                            job title:
                            <el-select v-model="searchAdvData.jobTitleId" placeholder="job title" size="mini"
                                       style="width: 130px;">
                                <el-option
                                        v-for="item in jobTitles"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="7">
                            Employment form:
                            <el-radio-group v-model="searchAdvData.engageForm">
                                <el-radio label="Labor Contract">Labor Contract</el-radio>
                                <el-radio label="Labor contract">Labor contract</el-radio>
                            </el-radio-group>
                        </el-col>
                    </el-row>
                    <el-row style="margin-top: 10px">
                        <el-col :span="5">
                            Department:
                            <el-popover
                                    placement="right"
                                    title="Please select a department"
                                    width="200"
                                    trigger="manual"
                                    v-model="popoverVisibleSearchAdv">
                                <el-tree default-expand-all :data="departments" :props="defaultProps"
                                         @node-click="handleNodeClickSearchAdv"></el-tree>
                                <div slot="reference" class="employee-searchadv-department"
                                     @click="handleSelectDepartmentSearchAdv">{{departmentNameSearchAdv}}
                                </div>
                            </el-popover>
                        </el-col>
                        <el-col :span="10">
                            Entry date:
                            <el-date-picker
                                    v-model="searchAdvData.beginDateScope"
                                    type="daterange"
                                    size="mini"
                                    unlink-panels
                                    value-format="yyyy-MM-dd"
                                    range-separator="to"
                                    start-placeholder="start date"
                                    end-placeholder="End date">
                            </el-date-picker>
                        </el-col>
                        <el-col :span="5" :offset="4">
                            <el-button size="mini" @click="searchAdvViewVisible = !searchAdvViewVisible">cancel</el-button>
                            <el-button size="mini" icon="el-icon-search" type="primary" @click="handleSearchAdvConfirm">
                                search for
                            </el-button>
                        </el-col>
                    </el-row>
                </div>
            </transition>
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
                    size="mini"
                    @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="name" label="Name" width="90" fixed></el-table-column>
                <el-table-column prop="workId" label="Job number" width="85"></el-table-column>
                <el-table-column prop="gender" label="gender" width="60" align="center"></el-table-column>
                <el-table-column prop="birthday" label="date of birth" width="95"></el-table-column>
                <el-table-column prop="idCard" label="identification number" width="150"></el-table-column>
                <el-table-column prop="wedlock" label="marital status" width="70" align="center"></el-table-column>
                <el-table-column prop="nation.name" label="Nation" width="60" align="center"></el-table-column>
                <el-table-column prop="nativePlace" label="Hometown" width="80" align="center"></el-table-column>
                <el-table-column prop="politicsStatus.name" label="political status" align="center"></el-table-column>
                <el-table-column prop="email" label="E-mail" width="180"></el-table-column>
                <el-table-column prop="phone" label="telephone number" width="100"></el-table-column>
                <el-table-column prop="address" label="contact address" width="220"></el-table-column>
                <el-table-column prop="department.name" label="Department" width="100"></el-table-column>
                <el-table-column prop="position.name" label="position" width="100"></el-table-column>
                <el-table-column prop="jobTitle.name" label="job title" width="100"></el-table-column>
                <el-table-column prop="engageForm" label="Employment form" width="100"></el-table-column>
                <el-table-column prop="tiptopDegree" label="highest education" width="80"></el-table-column>
                <el-table-column prop="specialty" label="profession" width="150"></el-table-column>
                <el-table-column prop="school" width="150" label="graduated school"></el-table-column>
                <el-table-column prop="beginDate" width="95" label="Entry date"></el-table-column>
                <el-table-column prop="conversionTime" width="95" label="Correct date"></el-table-column>
                <el-table-column prop="beginContract" width="95" label="Contract start date"></el-table-column>
                <el-table-column prop="endContract" width="95" label="Contract deadline"></el-table-column>
                <el-table-column label="Contract period" width="100">
                    <template slot-scope="scope">
                        <span>{{scope.row.contractTerm}}</span>year
                    </template>
                </el-table-column>
                <el-table-column label="operating" width="150" align="center" fixed="right">
                    <template slot-scope="scope">
                        <el-button class="employee-table-btn" size="mini" @click="handleEdit(scope.row)">edit</el-button>
                        <el-button class="employee-table-btn" size="mini" type="danger"
                                   @click="handleDelete(scope.row)">delete
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="employee-pagination-container">
                <el-button
                        type="danger"
                        :disabled="this.multipleSelection.length == 0"
                        @click="handleDeleteMulti">batch deletion
                </el-button>
                <el-pagination
                        background
                        layout="sizes, prev, pager, next, jumper, ->, total, slot"
                        :total="total"
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange">
                </el-pagination>
            </div>
        </div>
        <el-dialog
                :title="dialogTitle"
                :visible.sync="dialogVisible"
                width="80%">
            <div>
                <el-form :model="employee" :rules="rules" ref="employeeForm">
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="Name:" prop="name">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                          v-model="employee.name"
                                          placeholder="Please enter the employee's name"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="gender:" prop="gender">
                                <el-radio-group v-model="employee.gender">
                                    <el-radio label="male">male</el-radio>
                                    <el-radio label="Female">Female</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="date of birth:" prop="birthday">
                                <el-date-picker
                                        v-model="employee.birthday"
                                        size="mini"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        style="width: 150px;"
                                        placeholder="Please enter date of birth">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="political status:" prop="politicsStatusId">
                                <el-select v-model="employee.politicsStatusId" placeholder="Please choose a political outlook" size="mini"
                                           style="width: 200px;">
                                    <el-option
                                            v-for="item in politicsStatuses"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="Nation:" prop="nationId">
                                <el-select v-model="employee.nationId" placeholder="Nation" size="mini"
                                           style="width: 150px;">
                                    <el-option
                                            v-for="item in nations"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="Hometown:" prop="nativePlace">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                          v-model="employee.nativePlace" placeholder="Please enter hometown"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="E-mail:" prop="email">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-message"
                                          v-model="employee.email" placeholder="Please enter your email"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="contact address:" prop="address">
                                <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit"
                                          v-model="employee.address" placeholder="Please enter contact address"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="position:" prop="positionId">
                                <el-select v-model="employee.positionId" placeholder="Please select a position" size="mini"
                                           style="width: 150px;">
                                    <el-option
                                            v-for="item in positions"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="job title:" prop="jobTitleId">
                                <el-select v-model="employee.jobTitleId" placeholder="Please select a job title" size="mini"
                                           style="width: 150px;">
                                    <el-option
                                            v-for="item in jobTitles"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="Department:" prop="departmentId">
                                <el-popover
                                        placement="right"
                                        title="Please select a department"
                                        width="200"
                                        trigger="manual"
                                        v-model="popoverVisible">
                                    <el-tree default-expand-all :data="departments" :props="defaultProps"
                                             @node-click="handleNodeClick"></el-tree>
                                    <div slot="reference" class="employee-add-edit-department"
                                         @click="handleSelectDepartment">{{departmentName}}
                                    </div>
                                </el-popover>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="telephone number:" prop="phone">
                                <el-input size="mini" style="width: 200px" prefix-icon="el-icon-phone"
                                          v-model="employee.phone" placeholder="telephone number"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="Job number:" prop="workId">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                          v-model="employee.workId" placeholder="Job number" disabled></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="highest education:" prop="tiptopDegree">
                                <el-select v-model="employee.tiptopDegree" placeholder="Education" size="mini"
                                           style="width: 120px;">
                                    <el-option
                                            v-for="item in tiptopDegrees"
                                            :key="item"
                                            :label="item"
                                            :value="item">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="graduated school:" prop="school">
                                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                          v-model="employee.school" placeholder="Name of graduation institution"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="professional title:" prop="specialty">
                                <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit"
                                          v-model="employee.specialty" placeholder="Please enter the professional name"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="Entry date:" prop="beginDate">
                                <el-date-picker
                                        v-model="employee.beginDate"
                                        size="mini"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        style="width: 130px;"
                                        placeholder="Entry date">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="Correct date:" prop="conversionTime">
                                <el-date-picker
                                        v-model="employee.conversionTime"
                                        size="mini"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        style="width: 130px;"
                                        placeholder="Correct date">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="Contract start date:" prop="beginContract">
                                <el-date-picker
                                        v-model="employee.beginContract"
                                        size="mini"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        style="width: 130px;"
                                        placeholder="Contract start date">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="Contract termination date:" prop="endContract">
                                <el-date-picker
                                        v-model="employee.endContract"
                                        size="mini"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        style="width: 130px;"
                                        placeholder="Contract termination date">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="identification number:" prop="idCard">
                                <el-input size="mini" style="width: 180px" prefix-icon="el-icon-edit"
                                          v-model="employee.idCard" placeholder="Please enter the ID number"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="Employment form:" prop="engageForm">
                                <el-radio-group v-model="employee.engageForm">
                                    <el-radio label="Labor Contract">Labor Contract</el-radio>
                                    <el-radio label="Labor contract">Labor contract</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="marital status:" prop="wedlock">
                                <el-radio-group v-model="employee.wedlock">
                                    <el-radio label="married">married</el-radio>
                                    <el-radio label="unmarried">unmarried</el-radio>
                                    <el-radio label="Divorce">Divorce</el-radio>
                                </el-radio-group>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
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
        name: "EmpBasic",
        data() {
            return {
                searchData: {
                    name: ''
                },
                searchAdvData: {
                    politicsStatusId: null,
                    nationId: null,
                    jobTitleId: null,
                    positionId: null,
                    engageForm: null,
                    departmentId: null,
                    beginDateScope: null
                },
                departmentNameSearchAdv: 'Department',
                searchAdvViewVisible: false,
                importBtnText: 'Import Data',
                importBtnIcon: 'el-icon-download',
                importBtnDisabled: false,
                employees: [],
                page: 1,
                size: 10,
                total: 0,
                multipleSelection: [],
                dialogTitle: '',
                dialogVisible: false,
                employee: {
                    name: "",
                    gender: "",
                    birthday: "",
                    idCard: "",
                    wedlock: "",
                    nationId: 1,
                    nativePlace: "",
                    politicsStatusId: "",
                    email: "",
                    phone: "",
                    address: "",
                    departmentId: null,
                    jobTitleId: null,
                    positionId: null,
                    engageForm: "",
                    tiptopDegree: "",
                    specialty: "",
                    school: "",
                    beginDate: "",
                    workId: "",
                    contractTerm: "",
                    conversionTime: "",
                    notworkDate: null,
                    beginContract: "",
                    endContract: "",
                    workAge: null
                },
                departmentName: 'Department',
                nations: [],
                politicsStatuses: [],
                departments: [],
                jobTitles: [],
                positions: [],
                tiptopDegrees: ['Undergraduate', 'College', 'masters degree', 'PhD', 'High school', 'junior high school', 'primary school', 'other'],
                rules: {
                    name: [{required: true, message: 'Please type in your name', trigger: 'blur'}],
                    gender: [{required: true, message: 'Please enter gender', trigger: 'blur'}],
                    birthday: [{required: true, message: 'Please enter date of birth', trigger: 'blur'}],
                    idCard: [{required: true, message: 'Please enter the ID number', trigger: 'blur'}, {
                        pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
                        message: 'ID card format is incorrect',
                        trigger: 'blur'
                    }],
                    wedlock: [{required: true, message: 'Please enter marital status', trigger: 'blur'}],
                    nationId: [{required: true, message: 'Please enter ethnic', trigger: 'blur'}],
                    nativePlace: [{required: true, message: 'Please enter hometown', trigger: 'blur'}],
                    politicsStatusId: [{required: true, message: 'Please enter a political look', trigger: 'blur'}],
                    email: [{required: true, message: 'Please enter your email', trigger: 'blur'}, {
                        type: 'email',
                        message: 'Incorrect email format',
                        trigger: 'blur'
                    }],
                    phone: [{required: true, message: 'Please enter your phone number', trigger: 'blur'}],
                    address: [{required: true, message: 'Please enter contact address', trigger: 'blur'}],
                    departmentId: [{required: true, message: 'Please select a department', trigger: 'blur'}],
                    jobTitleId: [{required: true, message: 'Please enter a job title', trigger: 'blur'}],
                    positionId: [{required: true, message: 'Please enter a job title', trigger: 'blur'}],
                    engageForm: [{required: true, message: 'Please enter the employment form', trigger: 'blur'}],
                    tiptopDegree: [{required: true, message: 'Please enter the highest degree', trigger: 'blur'}],
                    specialty: [{required: true, message: 'Please enter major', trigger: 'blur'}],
                    school: [{required: true, message: 'Please enter the graduation college', trigger: 'blur'}],
                    beginDate: [{required: true, message: 'Please enter the entry date', trigger: 'blur'}],
                    workId: [{required: true, message: 'Please enter the job number', trigger: 'blur'}],
                    contractTerm: [{required: true, message: 'Please enter the contract period', trigger: 'blur'}],
                    conversionTime: [{required: true, message: 'Please enter the transfer date', trigger: 'blur'}],
                    notworkDate: [{required: true, message: 'Please enter the departure date', trigger: 'blur'}],
                    beginContract: [{required: true, message: 'Please enter the contract start date', trigger: 'blur'}],
                    endContract: [{required: true, message: 'Please enter the contract end date', trigger: 'blur'}],
                    workAge: [{required: true, message: 'Please enter seniority', trigger: 'blur'}],
                },
                popoverVisible: false,
                popoverVisibleSearchAdv: false,
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                loading: false
            }
        },
        mounted() {
            this.initEmployees();
        },
        methods: {
            initEmployees() {
                this.loading = true;
                let url = "/employee/basic/getPage?page=" + this.page + "&size=" + this.size;
                if (this.searchAdvViewVisible) {
                    if (this.searchAdvData.politicsStatusId) {
                        url += '&politicsStatusId=' + this.searchAdvData.politicsStatusId;
                    }
                    if (this.searchAdvData.nationId) {
                        url += '&nationId=' + this.searchAdvData.nationId;
                    }
                    if (this.searchAdvData.jobTitleId) {
                        url += '&jobTitleId=' + this.searchAdvData.jobTitleId;
                    }
                    if (this.searchAdvData.positionId) {
                        url += '&positionId=' + this.searchAdvData.positionId;
                    }
                    if (this.searchAdvData.engageForm) {
                        url += '&engageForm=' + this.searchAdvData.engageForm;
                    }
                    if (this.searchAdvData.departmentId) {
                        url += '&departmentId=' + this.searchAdvData.departmentId;
                    }
                    if (this.searchAdvData.beginDateScope) {
                        url += '&beginDateScope=' + this.searchAdvData.beginDateScope[0];
                        url += '&beginDateScope=' + this.searchAdvData.beginDateScope[1];
                    }
                } else {
                    url += "&name=" + this.searchData.name;
                }

                this.getRequest(url).then(resp => {
                    this.loading = false;
                    if (resp) {
                        this.employees = resp.data;
                        this.total = resp.total;
                    }
                });
            },
            initEmployee() {
                this.employee = {
                    name: "",
                    gender: "",
                    birthday: "",
                    idCard: "",
                    wedlock: "",
                    nationId: 1,
                    nativePlace: "",
                    politicsStatusId: "",
                    email: "",
                    phone: "",
                    address: "",
                    departmentId: null,
                    jobTitleId: null,
                    positionId: null,
                    engageForm: "",
                    tiptopDegree: "",
                    specialty: "",
                    school: "",
                    beginDate: "",
                    workId: "",
                    contractTerm: "",
                    conversionTime: "",
                    notworkDate: null,
                    beginContract: "",
                    endContract: "",
                    workAge: null
                };
                this.departmentName = 'Department';
            },
            initEmployeeTest() {
                this.employee = {
                    name: "Test staff",
                    gender: "male",
                    birthday: "1989-12-31",
                    idCard: "610122199001011256",
                    wedlock: "married",
                    nationId: 1,
                    nativePlace: "Shaanxi",
                    politicsStatusId: 13,
                    email: "laowang@qq.com",
                    phone: "18565558897",
                    address: "Nanshan District, Shenzhen",
                    departmentId: null,
                    jobTitleId: 9,
                    positionId: 29,
                    engageForm: "Labor contract",
                    tiptopDegree: "Undergraduate",
                    specialty: "Information management and information system",
                    school: "Shenzhen University",
                    beginDate: "2017-12-31",
                    workId: "00000057",
                    contractTerm: 2,
                    conversionTime: "2018-03-31",
                    notworkDate: null,
                    beginContract: "2017-12-31",
                    endContract: "2019-12-31",
                    workAge: null
                };
                this.departmentName = '';
            },
            initSearchAdvData() {
                this.searchAdvData = {
                    politicsStatusId: null,
                    nationId: null,
                    jobTitleId: null,
                    positionId: null,
                    engageForm: null,
                    departmentId: null,
                    beginDateScope: null
                };
                this.departmentNameSearchAdv = 'Department';
            },
            initNations() {
                if (!window.sessionStorage.getItem("nations")) {
                    this.getRequest('/employee/basic/getNationListAll').then(resp => {
                        if (resp) {
                            this.nations = resp;
                            window.sessionStorage.setItem("nations", JSON.stringify(resp));
                        }
                    })
                } else {
                    this.nations = JSON.parse(window.sessionStorage.getItem("nations"));
                }
            },
            initPoliticsStatuses() {
                if (!window.sessionStorage.getItem("politicsStatuses")) {
                    this.getRequest('/employee/basic/getPoliticsStatusListAll').then(resp => {
                        if (resp) {
                            this.politicsStatuses = resp;
                            window.sessionStorage.setItem("politicsStatuses", JSON.stringify(resp));
                        }
                    })
                } else {
                    this.politicsStatuses = JSON.parse(window.sessionStorage.getItem("politicsStatuses"));
                }
            },
            initDepartments() {
                if (!window.sessionStorage.getItem("departments")) {
                    this.getRequest('/employee/basic/getDepartmentListAll').then(resp => {
                        if (resp) {
                            this.departments = resp;
                            window.sessionStorage.setItem("departments", JSON.stringify(resp));
                        }
                    })
                } else {
                    this.departments = JSON.parse(window.sessionStorage.getItem("departments"));
                }
            },
            initJobTitles() {
                if (!window.sessionStorage.getItem("jobTitles")) {
                    this.getRequest('/employee/basic/getJobTitleListAll').then(resp => {
                        if (resp) {
                            this.jobTitles = resp;
                            window.sessionStorage.setItem("jobTitles", JSON.stringify(resp));
                        }
                    })
                } else {
                    this.jobTitles = JSON.parse(window.sessionStorage.getItem("jobTitles"));
                }
            },
            initPositions() {
                this.getRequest('/employee/basic/getPositionListAll').then(resp => {
                    if (resp) {
                        this.positions = resp;
                    }
                })
            },
            initWorkId() {
                this.getRequest('/employee/basic/getNextWorkId').then(resp => {
                    if (resp) {
                        this.employee.workId = resp.obj;
                    }
                })
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
            handleSearchAdv() {
                this.searchAdvViewVisible = !this.searchAdvViewVisible;
                if (this.searchAdvViewVisible) {
                    this.initSearchAdvData();
                    this.initNations();
                    this.initPoliticsStatuses();
                    this.initDepartments();
                    this.initJobTitles();
                    this.initPositions();
                }
            },
            handleSearchAdvConfirm() {
                this.initEmployees();
            },
            handleBeforeUpload() {
                this.importBtnText = 'Importing';
                this.importBtnIcon = 'el-icon-loading';
                this.importBtnDisabled = true;
            },
            handleSuccess(response, file, fileList) {
                this.importBtnText = 'Import Data';
                this.importBtnIcon = 'el-icon-download';
                this.importBtnDisabled = false;
                this.initEmployees();
            },
            handleError(err, file, fileList) {
                this.importBtnText = 'Import Data';
                this.importBtnIcon = 'el-icon-download';
                this.importBtnDisabled = false;
            },
            handleExport() {
                window.open('/employee/basic/export', '_parent');
            },
            handleAdd() {
                this.initEmployee();
                // this.initEmployeeTest();
                this.initNations();
                this.initPoliticsStatuses();
                this.initDepartments();
                this.initJobTitles();
                this.initPositions();
                this.initWorkId();
                this.dialogTitle = 'New employee';
                this.dialogVisible = true;
            },
            handleEdit(data) {
                this.initEmployee();
                this.initNations();
                this.initPoliticsStatuses();
                this.initDepartments();
                this.initJobTitles();
                this.initPositions();
                Object.assign(this.employee, data);// A copy of the data
                this.departmentName = data.department.name;
                this.dialogTitle = 'Edit employee';
                this.dialogVisible = true;
            },
            handleAddEditConfirm() {
                if (this.employee.id) {
                    this.$refs['employeeForm'].validate((valid) => {
                        if (valid) {
                            this.putRequest('/employee/basic/edit', this.employee).then(resp => {
                                if (resp) {
                                    this.dialogVisible = false;
                                    this.initEmployees();
                                }
                            });
                        } else {
                            this.$message.error('Form validation error');
                            return false;
                        }
                    });
                } else {
                    this.$refs['employeeForm'].validate((valid) => {
                        if (valid) {
                            this.postRequest('/employee/basic/add', this.employee).then(resp => {
                                if (resp) {
                                    this.dialogVisible = false;
                                    this.initEmployees();
                                }
                            });
                        } else {
                            this.$message.error('Form validation error');
                            return false;
                        }
                    });
                }
            },
            handleDelete(data) {
                this.$confirm('This operation will be permanently deleted【' + data.name + '】Employee, Whether to continue?', 'prompt', {
                    confirmButtonText: 'determine',
                    cancelButtonText: 'cancel',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/employee/basic/deleteById/" + data.id).then(resp => {
                        if (resp) {
                            this.initEmployees();
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
                    this.deleteRequest("/employee/basic/deleteByIds?" + ids).then(resp => {
                        if (resp) {
                            this.initEmployees();
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
            handleNodeClick(data) {
                this.employee.departmentId = data.id;
                this.departmentName = data.name;
                this.popoverVisible = !this.popoverVisible;
            },
            handleNodeClickSearchAdv(data) {
                this.searchAdvData.departmentId = data.id;
                this.departmentNameSearchAdv = data.name;
                this.popoverVisibleSearchAdv = !this.popoverVisibleSearchAdv;
            },
            handleSelectDepartment() {
                this.popoverVisible = !this.popoverVisible;
            },
            handleSelectDepartmentSearchAdv() {
                this.popoverVisibleSearchAdv = !this.popoverVisibleSearchAdv;
            }
        }
    }
</script>

<style scoped>
    .employee-toolbar-container {
        display: flex;
        justify-content: space-between;
    }

    .employee-search-name {
        width: 50%;
        margin-right: 10px;
    }

    .employee-searchadv {
        border: 1px solid #409eff;
        border-radius: 5px;
        box-sizing: border-box;
        padding: 5px 10px;
        margin: 10px 0px;
        font-size: 13px;
    }

    .employee-searchadv-department {
        width: 130px;
        display: inline-flex;
        font-size: 13px;
        border: 1px solid #dedede;
        height: 26px;
        border-radius: 5px;
        cursor: pointer;
        align-items: center;
        padding-left: 12px;
        box-sizing: border-box;
        color: #999;
    }

    .employee-table {
        width: 100%;
        margin-top: 10px;
    }

    .employee-table-btn {
        padding: 2px 10px;
    }

    .employee-pagination-container {
        display: flex;
        justify-content: space-between;
        margin-top: 10px;
    }

    .employee-add-edit-department {
        width: 150px;
        display: inline-flex;
        font-size: 13px;
        border: 1px solid #dedede;
        height: 26px;
        border-radius: 5px;
        cursor: pointer;
        align-items: center;
        padding-left: 12px;
        box-sizing: border-box;
        color: #999;
    }

    /* Different entry and exit animations can be set */
    /* Set duration and animation function */
    .slide-fade-enter-active {
        transition: all .5s ease;
    }

    .slide-fade-leave-active {
        transition: all .5s cubic-bezier(1.0, 0.5, 0.8, 1.0);
    }

    .slide-fade-enter, .slide-fade-leave-to
        /* .slide-fade-leave-active for below version 2.1.8 */
    {
        transform: translateX(10px);
        opacity: 0;
    }

</style>
