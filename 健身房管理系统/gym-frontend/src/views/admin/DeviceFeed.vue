<template>
  <div class="feed">
    <h3>设备资料</h3>
    <p>{{ message }}</p>
    <p class="hint">文件路径：{{ path }}</p>
    <p class="hint">器材写入 equipment，扫码写入 scans。两项都是空数组时，页面保持未导入。格式不对的行会跳过，已跳过 {{ skipped }} 条。</p>
    <pre class="sample">{
  "equipment": [{"name": "跑步机", "status": "在线", "place": "一楼", "updated": "2026-10-13 09:00:00", "note": ""}],
  "scans": [{"memberNo": 1, "memberPhone": "", "time": "2026-10-13 09:00:00", "device": "前台"}]
}</pre>
    <el-button type="primary" size="small" @click="sync">写入签到记录</el-button>
    <h4>器材状态（{{ equipment.length }}）</h4>
    <el-table :data="equipment" stripe>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="status" label="状态"></el-table-column>
      <el-table-column prop="place" label="位置"></el-table-column>
      <el-table-column prop="updated" label="更新时间"></el-table-column>
      <el-table-column prop="note" label="说明"></el-table-column>
    </el-table>
    <h4>扫码记录（{{ scans.length }}）</h4>
    <el-table :data="scans" stripe>
      <el-table-column prop="memberName" label="会员"></el-table-column>
      <el-table-column prop="time" label="时间"></el-table-column>
      <el-table-column prop="device" label="设备"></el-table-column>
      <el-table-column prop="result" label="结果"></el-table-column>
    </el-table>
  </div>
</template>

<script>
import {deviceFeed, syncDeviceScans} from "@/api/allApi";

export default {
  data() {
    return {
      message: '',
      path: '',
      skipped: 0,
      equipment: [],
      scans: []
    }
  },
  mounted() {
    this.load()
  },
  methods: {
    load() {
      deviceFeed({kind: 'equipment'}).then(res => {
        const data = res.data || {}
        this.message = data.message || ''
        this.path = data.path || ''
        this.skipped = data.skipped || 0
        this.equipment = data.equipment || []
        this.scans = data.scans || []
      }).catch(err => {
        console.log(err.message)
      })
    },
    sync() {
      syncDeviceScans().then(res => {
        alert((res.data && res.data.message) || '没有可写入的签到')
        this.load()
      }).catch(err => {
        console.log(err.message)
      })
    }
  }
}
</script>

<style scoped>
.feed {
  padding: 24px;
  background: #fff;
}
.hint {
  color: #909399;
}
.sample {
  background: #f7f7f7;
  padding: 12px;
  white-space: pre-wrap;
}
</style>
