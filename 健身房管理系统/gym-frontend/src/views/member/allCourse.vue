<template>
  <div class="allCourse">
    <div class="recommend" v-if="recommend.reason">
      <strong>课程推荐</strong>
      <p>{{ recommend.reason }}</p>
      <span v-for="item in recommend.items" :key="item.courseNo" class="tag">{{ item.courseName }} {{ item.coursePrice }}元</span>
    </div>
    <el-table
        :data="tableData"
        height="520px"
        stripe
        style="width: 100%">
      <el-table-column type="expand">
        <template slot-scope="props">
          <div style="width: 85%;margin: auto">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="课程名称">
                <span>{{ props.row.courseName }}</span>
              </el-form-item>
              <el-form-item label="课程价格">
                <span>{{ props.row.coursePrice }} 元</span>
              </el-form-item>
              <el-form-item label="开课时间">
                <span>{{ props.row.courseTime }}</span>
              </el-form-item>
              <el-form-item label="课程时长">
                <span>{{ props.row.courseDuration }} 分钟</span>
              </el-form-item>
              <el-form-item label="授课教练">
                <span>{{ props.row.employeeNameCoach }}</span>
              </el-form-item>
              <el-form-item label="联系方式">
                <span>{{ props.row.employeePhoneCoach }}</span>
              </el-form-item>
              <el-form-item label="课程积分">
                <span>{{ props.row.courseIntegral }}</span>
              </el-form-item>
              <el-form-item label="课程描述">
                <span>{{ props.row.courseDesc }}</span>
              </el-form-item>
            </el-form>
          </div>

        </template>
      </el-table-column>
      <el-table-column
          label="课程编号"
          prop="courseNo">
      </el-table-column>
      <el-table-column
          label="课程名称"
          prop="courseName">
      </el-table-column>
      <el-table-column
          label="课程描述"
          prop="courseDesc">
      </el-table-column>
      <el-table-column
          label="原价"
          prop="coursePrice">
      </el-table-column>
      <el-table-column
            label="优惠价"
            prop="discountPrice">
      </el-table-column>
      <el-table-column
          label="操作"
          prop="courseDesc">
        <template slot-scope="scope">
          <el-button type="primary" plain size="mini" @click="buyCourse(scope.row)">购买</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import moment from "moment";
import {
  addRegister,
  getAllCourse,
  getMemberPower,
  recommendCourse,
  updateMemberIntegral
} from "@/api/allApi";

export default {
  data() {
    return {
      tableData: [
      ],
      admin:{

      },
      memberPower:'',
      coursePrice:'',
      recommend:{reason:'', items:[]},
    }

  },
  computed: {
    indexMethod() {
      return (this.currentPage - 1) * 10 + 1
    }
  },
  filters: {
    dataFormat(value) {
      return moment(value).format("YYYY-MM-DD")
    }
  },
  created() {
    this.admin = JSON.parse(window.localStorage.getItem('access-member'))
  },
  mounted() {
    if (!this.admin) return
    recommendCourse({memberNo: this.admin.memberNo}).then(res => {
      this.recommend = res.data || {reason:'', items:[]}
    }).catch(() => {})
    getMemberPower({
      memberNo: this.admin.memberNo
    }).then(res => {
      this.memberPower = res.data
      this.getAllCourse()
    }).catch(() => {
      this.getAllCourse()
    })
  },
  methods:{
    rateOf(power) {
      const rates = {1: 0.9, 2: 0.8, 3: 0.7, 4: 0.6}
      return rates[Number(power)] || 1
    },
    buyCourse(row){
      let _this = this
      addRegister({
        courseNo: row.courseNo,
        memberPhone: _this.admin.memberPhone,
        memberNo: _this.admin.memberNo,
      }).then(res=>{
        if(res.data.code===200){
          updateMemberIntegral({
            memberNo: this.admin.memberNo,
            price: -row.courseIntegral
          })
          alert('购买成功，已按会员折扣扣款 ' + row.discountPrice + ' 元')
        }else{
          alert(res.data.message)
        }
      }).catch(err=>{
        console.log(err.message)
      })

    },
    getAllCourse() {
      getAllCourse({
        page: 0,
        size: 50
      }).then(res => {
        const rate = this.rateOf(this.memberPower)
        this.tableData = (res.data || []).map(row => ({
          ...row,
          discountPrice: Math.round(row.coursePrice * rate * 100) / 100
        }))
      }).catch(err => {
        console.log(err.message)
      })
    }

  }
}
</script>

<style scoped>

  .allCourse {
    padding: 40px;
    width: 1000px;
  }

  .recommend {
    margin-bottom: 16px;
    padding: 12px 16px;
    background: #fff;
    border: 1px solid #ebeef5;
  }

  .tag {
    display: inline-block;
    margin-right: 8px;
    color: #409EFF;
  }

  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>