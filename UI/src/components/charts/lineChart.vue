<template>
  <div class="c" style="height: 100%; width: 100%">
    <div id="mainC" style="height: 100%; width: 100%;"></div>
  </div>
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons')
export default {
  name: "barChart",
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
          text: '折线图堆叠'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['邮件营销', '联盟广告', '视频广告', '直接访问', '搜索引擎']
      },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: []
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '邮件营销',
            type: 'line',
            stack: '总量',
            
            data: [120, 132, 101, 134, 90, 230, 210]
          },
          {
            name: '联盟广告',
            type: 'line',
            stack: '总量',
           
            data: [220, 182, 191, 234, 290, 330, 310]
          },
          {
            name: '视频广告',
            type: 'line',
            stack: '总量',
             
            data: [150, 232, 201, 154, 190, 330, 410]
          },
          {
            name: '直接访问',
            type: 'line',
            stack: '总量',
         
            data: [320, 332, 301, 334, 390, 330, 320]
          },
       
        ]
      }
    }
  },
  
  mounted() {
     this.$nextTick(() => {
      this.setChart()
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
    setChart(){
        this.chart =echarts.init(this.$el, 'macarons');
     this.chart.setOption(this.option)
    }
  },
}

</script>

<style scoped>

</style>
