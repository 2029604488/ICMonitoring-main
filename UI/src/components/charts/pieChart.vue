<template>
  <div class="a" style="height: 100%; height: 100%">
    <div id="mainA" style="width: 100%; height: 100%"></div>
  </div>
</template>

<script>
import  echarts from 'echarts'
require('echarts/theme/macarons')
export default {
  name: "pieChart",
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
      option: {
        
         stompClient: '',
         ChartLineGraph:null,
         title: {
          text: '芯片温度分布'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
         toolbox: {
               feature: {
               saveAsImage: {}
              }
            },
        series: [
          {
            name: '访问来源',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '40',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              {value: 1048, name: '搜索引擎'},
              {value: 580, name: '邮件营销'},
              {value: 484, name: '联盟广告'},
              {value: 300, name: '视频广告'}
            ]
          }
        ]
      }
    }
  },
  methods:{
    setChart(){
      this.chart = echarts.init(this.$el, 'macarons');
    this.chart.setOption(this.option);
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
}
</script>

<style scoped>

</style>
