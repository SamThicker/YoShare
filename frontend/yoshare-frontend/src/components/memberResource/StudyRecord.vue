<template>
  <div class="studyRecord-wrap">
    <div id="chart"></div>

    <div class="reading-record" v-show="showRecords">
      <ul>
        <li class="record-item" v-for="record in recordDetails" :key="record.id">
          <span class="record-item-title">{{ getTitle(record) }}</span>
          <el-button type="primary" @click="read(record)">再读</el-button>
        </li>
      </ul>
    </div>

  </div>
</template>

<script>
import echarts from "echarts";
import { queryReadNoteDetail, queryReadNoteLog } from "../../api/log";

export default {
  name: "StudyRecord",
  data() {
    return {
      charts: "",
      readRecords: [],
      dates: [],
      counts: [],
      showRecords: false,
      recordDetails: []
    };
  },
  computed: {
    userId: function() {
      return this.$store.state.user.info.id;
    }
  },
  watch: {
    userId: function() {
      this.getRecord();
    },
    readRecords: function(val) {
      if (!val) return;
      this.dates.length = 0;
      this.counts.length = 0;
      val.forEach(record => {
        this.dates.push(record.date);
        this.counts.push(record.count);
      });
      this.$nextTick(function() {
        this.drawLine("chart");
      });
    }
  },
  methods: {
    getRecord: function() {
      let id = this.userId;
      if (!id) return;
      let _this = this;
      queryReadNoteLog(id, "MONTH")
        .then(function(res) {
          _this.readRecords = res.data;
        })
        .catch(err => console.info(err));
    },
    getTitle(record) {
      if (!record) return;
      return record.title;
    },
    eConsole(param) {
      param.event.event.stopPropagation();
      if (typeof param.seriesIndex == "undefined") {
        return;
      }
      let _this = this;
      if (param.type === "click") {
        let date = param.name;
        let userId = this.$store.state.user.info.id;
        document.addEventListener("click", this.clickListerner);
        queryReadNoteDetail(userId, date)
          .then(function(res) {
            _this.recordDetails = res.data;
            _this.showRecords = true;
          })
          .catch();
      }
    },
    read(record) {
      let noteId = record.noteId;
      let groupId = record.groupId;
      if (record.noteType === "MEMBER") {
        this.$router.push("/resource/note/" + noteId);
      } else if (record.noteType === "GROUP") {
        this.$router.push("/group/" + groupId + "/note/" + noteId);
      }
    },
    drawLine(id) {
      this.charts = echarts.init(document.getElementById(id));
      this.charts.on("click", this.eConsole);
      this.charts.setOption({
        tooltip: {
          trigger: "axis"
        },
        legend: {
          data: ["近一个月笔记阅读记录"]
        },
        grid: {
          left: "3%",
          right: "7%",
          bottom: "3%",
          containLabel: true
        },

        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: "category",
          boundaryGap: false,
          data: this.dates,
          name: "日期"
        },
        yAxis: {
          type: "value",
          name: "数量"
        },
        series: [
          {
            //可点击
            clickable: true,
            name: "近一个月笔记阅读记录",
            type: "line",
            stack: "总量",
            data: this.counts
          }
        ]
      });
    },
    clickListerner(e) {
      let el = document.getElementsByClassName("reading-record")[0];
      if (el.contains(e.target)) return;
      this.showRecords = false;
      document.removeEventListener("click", this.clickListerner);
    }
  },
  mounted() {
    this.getRecord();
  },
  destroyed() {
    document.removeEventListener("click", this.clickListerner);
  }
};
</script>

<style scoped>
#chart {
  width: 700px;
  height: 500px;
}

ul,
li {
  padding: 0;
  margin: 0;
  list-style: none;
}

.reading-record {
  overflow-y: auto;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate3d(-50%, -50%, 0);
  width: 500px;
  height: 450px;
  background-color: #e8f2fe;
  border-radius: 7px;
  box-shadow: 1px 4px 10px 2px #ccc;
  -webkit-box-shadow: 1px 4px 10px 2px #ccc;
  padding: 15px;
  box-sizing: border-box;
}

.record-item {
  text-align: left;
  box-sizing: border-box;
  width: 100%;
  height: 40px;
  line-height: 40px;
  margin-bottom: 3px;
  overflow: hidden;
  display: flex;
  padding: 2px;
  border-radius: 7px;
}

.record-item:hover {
  background-color: #d1e7fe;
}

.record-item-title {
  font-weight: bold;
  font-size: 17px;
  overflow: hidden;
  border-radius: 7px;
  background-color: white;
  width: 100px;
  flex: 1;
  padding-left: 8px;
  margin-right: 5px;
}
</style>
