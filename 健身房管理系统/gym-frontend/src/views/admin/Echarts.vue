<template>
  <div class="stats">
    <h3>近7天签到</h3>
    <div id="checkChart" class="chart"></div>
    <h3>课程报名</h3>
    <p v-if="!courses.length" class="empty">还没有报名记录。</p>
    <div v-show="courses.length" id="courseChart" class="chart"></div>
  </div>
</template>

<script>
import {gymStats} from "@/api/allApi";

export default {
  data() {
    return {
      courses: []
    }
  },
  mounted() {
    gymStats().then(res => {
      const days = (res.data && res.data.days) || []
      this.courses = (res.data && res.data.courses) || []
      this.draw('checkChart', '签到人次', days.map(item => item.label), days.map(item => item.total))
      this.$nextTick(() => {
        if (this.courses.length) {
          this.draw('courseChart', '报名次数', this.courses.map(item => item.label), this.courses.map(item => item.total))
        }
      })
    }).catch(err => {
      console.log(err.message)
    })
  },
  methods: {
    draw(id, name, labels, values) {
      const chart = this.$echarts.init(document.getElementById(id))
      chart.setOption({
        tooltip: {},
        xAxis: {data: labels},
        yAxis: {minInterval: 1},
        series: [{name: name, type: 'bar', data: values}]
      })
    }
  }
}
</script>

<style scoped>
.stats {
  padding: 24px;
  background: #fff;
}
.chart {
  width: 720px;
  height: 320px;
}
.empty {
  color: #909399;
}
</style>
