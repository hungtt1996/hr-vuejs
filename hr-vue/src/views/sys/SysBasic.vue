<template>
    <div>
        <el-tabs v-model="activeName" type="card">
            <el-tab-pane label="Department management" name="department"><Department></Department></el-tab-pane>
            <el-tab-pane label="Role management" name="role"><Role></Role></el-tab-pane>
            <el-tab-pane label="Position management" name="position"><Position></Position></el-tab-pane>
            <el-tab-pane label="Job title management" name="jobTitle"><JobTitle></JobTitle></el-tab-pane>
            <el-tab-pane label="Reward and punishment rules" name="4"><EcMana></EcMana></el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    import Department from "../../components/sys/Department";
    import Role from "../../components/sys/Role";
    import Position from "../../components/sys/Position";
    import JobTitle from "../../components/sys/JobTitle";
    import EcMana from "../../components/sys/EcMana";

    export default {
        name: "SysBasic",
        data() {
            return {
                activeName: 'department'
            }
        },
        components: {
            Department,
            Role,
            Position,
            JobTitle,
            EcMana
        }
    }
</script>

<style scoped>

</style>
