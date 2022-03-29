<template>
  <div >
      <div id="test1" style="height: 400px; width: 400px; display: inline-block"></div>
      <div id="test2" style="height: 400px; width: 400px; display: inline-block"></div>
      <div id="test3" style="height: 400px; width: 400px; display: inline-block"></div>
      <div id="test4" style="height: 400px; width: 400px; display: inline-block"></div>
  </div>
</template>

<script>
import echarts from 'echarts';

let ecStat = require("echarts-stat");

export default {
  name: "forecas",
  data() {
    return {
      data1: [
        [1990, 97.50795611],
        [1991, -1.47594249],
        [1992, 100.18523178],
        [1993, 70.79915311],
        [1994, 78.57069599],
        [1995, 31.10961999],
        [1996, 256.79296329],
        [1997, -125.99480511],
        [1998, 123.06305912],
        [1999, 20.88276861],
        [2000, 83.58488798],
        [2001, -27.35862341],
        [2002, 36.12902213],
        [2003, 330.29682761],
        [2004, 81.03370443],
        [2005, 72.43589272],
        [2006, 68.20541466],
        [2007, 71.72078271],
        [2008, 30.23107631],
        [2009, 73.36549425],
        [2010, 117.4990002],
        [2011, 210.40146031],
        [2012, 31.12226507],
        [2013, 86.32869462],
        [2014, 128.90111788],
        [2015, 115.70773146]
      ],
      data2: [
        [1, 4862.4],
        [2, 5294.7],
        [3, 5934.5],
        [4, 7171.0],
        [5, 8964.4],
        [6, 10202.2],
        [7, 11962.5],
        [8, 14928.3],
        [9, 16909.2],
        [10, 18547.9],
        [11, 21617.8],
        [12, 26638.1],
        [13, 34634.4],
        [14, 46759.4],
        [15, 58478.1],
        [16, 67884.6],
        [17, 74462.6],
        [18, 79395.7]
      ],
      data3: [
        [28604, 67],
        [31163, 67.4],
        [1516, 68],
        [13670, 74.7],
        [28599, 75],
        [29476, 77.1],
        [31476, 75.4],
        [28666, 78.1],
        [1777, 57.7],
        [29550, 79.1],
        [2076, 67.9],
        [12087, 72],
        [24021, 75.4],
        [43296, 76.8],
        [10088, 70.8],
        [19349, 69.6],
        [10670, 67.3],
        [26424, 75.7],
        [37062, 75.4],
        [44056, 81.8],
        [43294, 81.7],
        [13334, 76.9],
        [21291, 78.5],
        [38923, 80.8],
        [37599, 81.9],
        [44053, 81.1],
        [42182, 82.8],
        [5903, 66.8],
        [36162, 83.5],
        [1390, 71.4],
        [34644, 80.7],
        [34186, 80.6],
        [64304, 81.6],
        [24787, 77.3],
        [23038, 73.13],
        [19360, 76.5],
        [38225, 81.4],
        [53354, 79.1]
      ],
      data4: [
        [96.24, 11.35],
        [33.09, 85.11],
        [57.60, 36.61],
        [36.77, 27.26],
        [20.10, 6.72],
        [45.53, 36.37],
        [110.07, 80.13],
        [72.05, 20.88],
        [39.82, 37.15],
        [48.05, 70.50],
        [0.85, 2.57],
        [51.66, 63.70],
        [61.07, 127.13],
        [64.54, 33.59],
        [35.50, 25.01],
        [226.55, 664.02],
        [188.60, 175.31],
        [81.31, 108.68]
      ],
      regType: ["linear", "exponential", "logarithmic", "polynomial"],
      chartTitle: ["市场行情", "损耗", "环境温度", "人流量"],
      xName: ["年份", "运行时间/分钟", "运行时间/分钟", "时间/日"],
      yName: ["寿命变化/月", "功率/瓦", "摄氏度/℃", "人次"]
    }
  },
  mounted() {
    this.setRegressionChart(this.data1, this.chartTitle[0], "test1", this.regType[0], 1, "category", this.xName[0], this.yName[0]);
    this.setRegressionChart(this.data2, this.chartTitle[1], "test2", this.regType[1], 1, "category", this.xName[1], this.yName[1]);
    this.setRegressionChart(this.data3, this.chartTitle[2], "test3", this.regType[2], 1, "value", this.xName[2], this.yName[2]);
    this.setRegressionChart(this.data4, this.chartTitle[3], "test4", this.regType[3], 3, "value", this.xName[3], this.yName[3]);
  },
  methods: {
    setRegressionChart(chartData, chartTitle, chartId, regType, regOrder, xType, xName, yName) {

      let regressionData = ecStat.regression(regType, chartData, regOrder);
      regressionData.points.sort(function (a, b) {
        return a[0] - b[0];
      });

      let option = {
        title: {
          left: "25%",
          top: "2%",
          text: chartTitle,
          textStyle: {
            color: "#707070",
            fontSize: 25
          }
        },
        xAxis: {
          type: xType,
          name: xName,
          splitNumber: 7,
          splitLine: {
            show: true
          }
        },

        yAxis: {
          type: "value",
          name: yName,
          splitNumber: 7,
          splitLine: {
            show: true
          }
        },
        series: [{
          name: "scatter",
          type: "scatter",
          itemStyle: {
            color: "#87CEFF"
          },
          data: chartData
        },
          {
            name: "line",
            type: "line",
            showSymbol: false,
            lineStyle: {
              color: "#f00"
            },
            data: regressionData.points
          }
        ]
      };
      let myChart = echarts.init(
          document.getElementById(chartId)
      );
      myChart.setOption(option, true);
    }
  }
}


</script>

<style scoped>

</style>
