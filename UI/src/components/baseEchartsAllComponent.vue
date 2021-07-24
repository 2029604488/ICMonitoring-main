<template>
  <div class="a" style="height: 100%; height: 100%">
    <div id="mainA" style="width: 100%; height: 100%"></div>
  </div>
</template>
 
<script>
import echarts from 'echarts'
    export default {
        name: "baseEchartsAllComponent",
         props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    }
  },
      data(){
          return{
             stompClient: '',
            ChartLineGraph:null,
            option:{
              title: {
                text: '芯片对比图',
                left: 'center'
              },
              xAxis: {
                type: 'category',
                data: ['检品机1', '检品机2', '检品机3', '检品机4', '检品机5', '检品机6', '检品机7']
              },
              yAxis: {
                type: 'value'
              },
              legend: {
                left: 'center',
                data: ['本年', '上年'],
                bottom:0
              },
              grid: {
                left: '1%',
                right: '2%',
                bottom: '8%',
                containLabel: true
              },
              series: [
                {
                  name: '本年',
                  data: [1000, 200, 150, 80, 70, 110, 130],
                  type: 'bar',
                  barWidth:30,
                },
                 {
                  name: '本年',
                  data: [1000, 200, 150, 80, 70, 110, 130],
                  type: 'bar',
                  barWidth:30,
                },
                 {
                  name: '本年',
                  data: [1000, 200, 150, 80, 70, 110, 130],
                  type: 'bar',
                  barWidth:30,
                },
                {
                  name: '上年',
                  data: [120, 200, 150, 80, 70, 110, 130],
                  type: 'bar',
                  barWidth:30,
                }]
            }
          }
      },
      mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy: function () {
    // 页面离开时断开连接,清除定时器
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
    this.disconnect();
  },
    //  深度监听 父组件刚开始没有值，只有图标的配置项
    //  父组件ajax请求后改变数据的值，传递过来，图标已生成，监听传过来的值的改变
// deep:true.深度监听，确保data中子项修改也能监听到。写法参考：https://cn.vuejs.org/v2/api/#watch
      watch: {
    option: {
      handler() {
        this.chart.setOption(this.option);
        this.chart.resize()
      }, deep: true
    }
  },
     
       methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption(
          this.option
      )
    },
  },
     
    }
</script>
 
<style scoped>
 
</style>