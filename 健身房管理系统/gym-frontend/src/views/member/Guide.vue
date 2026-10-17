<template>
  <div class="guide">
    <h3>课程助手</h3>
    <p>{{ message }}</p>
    <p class="hint">只打开已有的购课、充值页面，签到使用左侧「今日签到」。不制定训练计划，也不判断伤病。问答资料为空时不编造回答。</p>
    <el-input v-model="question" placeholder="输入要检索的问题" style="width: 360px"></el-input>
    <el-button type="primary" size="small" @click="load">检索</el-button>
    <div v-if="matches.length" class="block">
      <div v-for="item in matches" :key="item.title" class="article">
        <strong>{{ item.title }}</strong>
        <p>{{ item.text }}</p>
      </div>
    </div>
    <div class="block">
      <strong>课程推荐</strong>
      <p>{{ recommend.reason }}</p>
      <el-tag v-for="item in recommend.items" :key="item.courseNo" class="tag">
        {{ item.courseName }} {{ item.coursePrice }}元
      </el-tag>
    </div>
    <div class="block">
      <el-button size="small" @click="open('allCourse')">去购课</el-button>
      <el-button size="small" @click="open('onlinePay')">去充值</el-button>
    </div>
    <pre class="sample">{
  "articles": [{"title": "课程说明", "text": "这里填写原文"}]
}</pre>
  </div>
</template>

<script>
import {gymGuide} from "@/api/allApi";

export default {
  data() {
    return {
      question: '',
      message: '',
      matches: [],
      recommend: {reason: '', items: []},
      member: {}
    }
  },
  created() {
    this.member = JSON.parse(window.localStorage.getItem('access-member')) || {}
  },
  mounted() {
    this.load()
  },
  methods: {
    load() {
      if (!this.member.memberNo) return
      gymGuide({
        memberNo: this.member.memberNo,
        question: this.question
      }).then(res => {
        const data = res.data || {}
        this.message = data.message || ''
        this.matches = data.matches || []
        this.recommend = data.recommend || {reason: '', items: []}
      }).catch(err => {
        console.log(err.message)
      })
    },
    open(url) {
      this.$router.push({path: url})
    }
  }
}
</script>

<style scoped>
.guide {
  padding: 40px;
  width: 1000px;
}
.hint, .sample {
  color: #909399;
}
.sample {
  background: #f7f7f7;
  padding: 12px;
  white-space: pre-wrap;
}
.block {
  margin-top: 16px;
}
.tag {
  margin-right: 8px;
}
</style>
