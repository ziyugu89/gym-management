<template>
  <div class="home">
    <!-- 背景装饰 -->
    <div class="background-decoration"></div>
    
    <!-- 欢迎区域 -->
    <el-card shadow="hover" class="welcome-card">
      <div class="welcome-content">
        <div class="welcome-text">
          <h1>欢迎使用健身房后台管理系统</h1>
          <p>今天是 {{ currentDate }}，{{ welcomeMessage }}</p>
        </div>
        <div class="welcome-icon">
          <img src="@/assets/logo.png" alt="健身房Logo" class="logo-img" />
        </div>
      </div>
    </el-card>

    <!-- 数据统计卡片 -->
    <div class="stats-container">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card member-card animate-card">
            <div class="stat-content">
              <div class="stat-icon">
                <i class="el-icon-user"></i>
              </div>
              <div class="stat-info">
                <h3>{{ memberCount }}</h3>
                <p>会员总数</p>
              </div>
            </div>
            <div class="card-decoration"></div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card course-card animate-card">
            <div class="stat-content">
              <div class="stat-icon">
                <i class="el-icon-reading"></i>
              </div>
              <div class="stat-info">
                <h3>{{ courseCount }}</h3>
                <p>课程总数</p>
              </div>
            </div>
            <div class="card-decoration"></div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card employee-card animate-card">
            <div class="stat-content">
              <div class="stat-icon">
                <i class="el-icon-s-custom"></i>
              </div>
              <div class="stat-info">
                <h3>{{ employeeCount }}</h3>
                <p>员工总数</p>
              </div>
            </div>
            <div class="card-decoration"></div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card register-card animate-card">
            <div class="stat-content">
              <div class="stat-icon">
                <i class="el-icon-tickets"></i>
              </div>
              <div class="stat-info">
                <h3>{{ registerCount }}</h3>
                <p>报名总数</p>
              </div>
            </div>
            <div class="card-decoration"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 健身房展示区域 -->
    <div class="gym-showcase">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card shadow="hover" class="showcase-card">
            <div slot="header" class="card-header">
              <span>健身房环境</span>
            </div>
            <div class="showcase-content">
              <img src="@/assets/back.png" alt="健身房环境" class="showcase-img" />
              <div class="showcase-overlay">
                <h3>专业健身设备</h3>
                <p>为您提供最优质的健身体验</p>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover" class="showcase-card">
            <div slot="header" class="card-header">
              <span>会员服务</span>
            </div>
            <div class="showcase-content">
              <img src="@/assets/temp.png" alt="会员服务" class="showcase-img" />
              <div class="showcase-overlay">
                <h3>贴心会员服务</h3>
                <p>专业教练一对一指导</p>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 快捷操作 -->
    <el-card shadow="hover" class="quick-actions">
      <div slot="header" class="card-header">
        <span>快捷操作</span>
        <i class="el-icon-s-operation header-icon"></i>
      </div>
      <div class="actions-content">
        <el-button type="primary" icon="el-icon-plus" @click="goToAddMember" class="action-btn">
          新增会员
        </el-button>
        <el-button type="success" icon="el-icon-edit" @click="goToCourseManage" class="action-btn">
          课程管理
        </el-button>
        <el-button type="warning" icon="el-icon-user" @click="goToEmployeeManage" class="action-btn">
          员工管理
        </el-button>
        <el-button type="info" icon="el-icon-tickets" @click="goToRegisterManage" class="action-btn">
          报名管理
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getAllMember, getAllCourse, getAllEmployee, getAllRegister, totalMember, totalCourse, totalEmployee, totalRegister } from '@/api/allApi'

export default {
  name: "Index",
  data() {
    return {
      currentDate: '',
      welcomeMessage: '',
      memberCount: 0,
      courseCount: 0,
      employeeCount: 0,
      registerCount: 0
    }
  },
  mounted() {
    this.initPage()
    this.loadStatistics()
    this.addAnimations()
  },
  methods: {
    initPage() {
      // 设置当前日期
      const now = new Date()
      this.currentDate = now.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        weekday: 'long'
      })
      
      // 设置欢迎消息
      const hour = now.getHours()
      if (hour < 12) {
        this.welcomeMessage = '早上好！'
      } else if (hour < 18) {
        this.welcomeMessage = '下午好！'
      } else {
        this.welcomeMessage = '晚上好！'
      }
    },
    
    async loadStatistics() {
      try {
        // 加载统计数据
        const [memberRes, courseRes, employeeRes, registerRes] = await Promise.all([
          totalMember(),
          totalCourse(),
          totalEmployee(),
          totalRegister()
        ])
        
        this.memberCount = memberRes.data.dataTotal || 0
        this.courseCount = courseRes.data.dataTotal || 0
        this.employeeCount = employeeRes.data.dataTotal || 0
        this.registerCount = registerRes.data.dataTotal || 0
      } catch (error) {
        console.error('加载统计数据失败:', error)
        this.$message.error('加载统计数据失败')
      }
    },
    
    addAnimations() {
      this.$nextTick(() => {
        const cards = document.querySelectorAll('.animate-card')
        cards.forEach((card, index) => {
          setTimeout(() => {
            card.style.opacity = '1'
            card.style.transform = 'translateY(0)'
          }, index * 200)
        })
      })
    },
    
    // 快捷操作方法
    goToAddMember() {
      this.$router.push('/layout/addMembers')
    },
    
    goToCourseManage() {
      this.$router.push('/layout/courseManage')
    },
    
    goToEmployeeManage() {
      this.$router.push('/layout/employeeManage')
    },
    
    goToRegisterManage() {
      this.$router.push('/layout/registerManage')
    }
  }
}
</script>

<style scoped>
.home {
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
  position: relative;
  overflow: hidden;
}

/* 背景装饰 */
.background-decoration {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    radial-gradient(circle at 20% 80%, rgba(120, 119, 198, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 80% 20%, rgba(255, 119, 198, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 40% 40%, rgba(120, 219, 255, 0.1) 0%, transparent 50%);
  z-index: -1;
}

/* 欢迎卡片 */
.welcome-card {
  margin-bottom: 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.3);
  transition: all 0.3s ease;
}

.welcome-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 40px rgba(102, 126, 234, 0.4);
}

.welcome-card ::v-deep .el-card__body {
  padding: 40px;
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-text h1 {
  font-size: 32px;
  margin-bottom: 15px;
  font-weight: 300;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.welcome-text p {
  font-size: 18px;
  opacity: 0.9;
}

.logo-img {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  box-shadow: 0 5px 15px rgba(255, 255, 255, 0.2);
  transition: transform 0.3s ease;
}

.logo-img:hover {
  transform: rotate(360deg) scale(1.1);
}

/* 统计卡片 */
.stats-container {
  margin-bottom: 30px;
}

.stat-card {
  transition: all 0.3s ease;
  border-radius: 15px;
  border: none;
  position: relative;
  overflow: hidden;
  opacity: 0;
  transform: translateY(30px);
}

.animate-card {
  transition: all 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

.stat-card:hover {
  transform: translateY(-10px) scale(1.02);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 25px;
  position: relative;
  z-index: 2;
}

.stat-icon {
  font-size: 45px;
  margin-right: 25px;
  width: 70px;
  height: 70px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease;
}

.stat-card:hover .stat-icon {
  transform: scale(1.1) rotate(5deg);
}

.member-card .stat-icon {
  background: linear-gradient(45deg, #409EFF, #66b3ff);
}

.course-card .stat-icon {
  background: linear-gradient(45deg, #67C23A, #85ce61);
}

.employee-card .stat-icon {
  background: linear-gradient(45deg, #E6A23C, #ebb563);
}

.register-card .stat-icon {
  background: linear-gradient(45deg, #F56C6C, #f78989);
}

.stat-info h3 {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #303133;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.stat-info p {
  font-size: 16px;
  color: #909399;
  font-weight: 500;
}

.card-decoration {
  position: absolute;
  top: 0;
  right: 0;
  width: 100px;
  height: 100px;
  background: linear-gradient(45deg, rgba(255, 255, 255, 0.1), transparent);
  border-radius: 0 15px 0 100px;
}

/* 健身房展示区域 */
.gym-showcase {
  margin-bottom: 30px;
}

.showcase-card {
  border-radius: 15px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.showcase-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
}

.showcase-content {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.showcase-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.showcase-card:hover .showcase-img {
  transform: scale(1.05);
}

.showcase-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  color: white;
  padding: 20px;
  transform: translateY(100%);
  transition: transform 0.3s ease;
}

.showcase-card:hover .showcase-overlay {
  transform: translateY(0);
}

.showcase-overlay h3 {
  margin-bottom: 5px;
  font-size: 18px;
}

.showcase-overlay p {
  margin: 0;
  opacity: 0.9;
}

/* 快捷操作 */
.quick-actions {
  border-radius: 15px;
  border: none;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
}

.card-header {
  font-weight: bold;
  color: #303133;
  font-size: 18px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-icon {
  font-size: 20px;
  color: #409EFF;
}

.actions-content {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  padding: 10px 0;
}

.action-btn {
  flex: 1;
  min-width: 140px;
  height: 50px;
  border-radius: 25px;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .welcome-content {
    flex-direction: column;
    text-align: center;
  }
  
  .logo-img {
    margin-top: 20px;
    width: 60px;
    height: 60px;
  }
  
  .actions-content {
    flex-direction: column;
  }
  
  .action-btn {
    width: 100%;
    min-width: auto;
  }
  
  .stat-content {
    padding: 20px;
  }
  
  .stat-icon {
    width: 60px;
    height: 60px;
    font-size: 35px;
  }
  
  .stat-info h3 {
    font-size: 28px;
  }
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.welcome-card {
  animation: fadeInUp 0.8s ease-out;
}

.quick-actions {
  animation: fadeInUp 1s ease-out;
}
</style>
