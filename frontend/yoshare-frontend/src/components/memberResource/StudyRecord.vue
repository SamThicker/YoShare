<template>
  <div class="studyRecord-wrap">
    <div id="chart"></div>
  </div>
</template>

<script>
import echarts from "echarts";
import { queryReadNoteLog } from "../../api/log";

export default {
  name: "StudyRecord",
  data() {
    return {
      charts: "",
      readRecords: [],
      dates: [],
      counts: []
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
    drawLine(id) {
      this.charts = echarts.init(document.getElementById(id));
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
            name: "近一个月笔记阅读记录",
            type: "line",
            stack: "总量",
            data: this.counts
          }
        ]
      });
    }
  },
  mounted() {
    this.getRecord();
  }
};
</script>

<style scoped>
#chart {
  width: 700px;
  height: 500px;
}
</style>
