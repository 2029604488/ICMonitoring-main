<template>

    <div class="container-o">

        <div id="one" class="level0" ></div>
        <div id="two" class="level0" ></div>
        <div id="three" class="level0"></div>
        <div id="four" class="level0"></div>
       <smile ref="smile1" style="position: absolute; top: 0px; left: 0px"></smile>
         <smile_r ref="smile3" style="position: absolute; top: 0px; right: 100px"></smile_r>
          <smile_r ref="smile4" style="position: absolute; top: 480px; right: 100px"></smile_r>
             <smile ref="smile2" style="position: absolute; top: 480px; left:0px"></smile>
      <div class="item-o e" style="">
        <div class="warnName">电压</div>
        <warning ref="g1"></warning>
      </div>
      <div class="item-o f"  style="">
        <div class="warnName">电流</div>
        <warning ref="g2"></warning>
      </div>
      <div class="item-o g"  style="">
        <div class="warnName">频率1</div>
        <warning ref="g3"></warning>
      </div>
      <div class="item-o h"  style="">
        <div class="warnName">频率2</div>
        <warning ref="g4"></warning>
      </div>

    </div>
</template>

<script>
  import moment from "moment";
  import SockJS from 'sockjs-client';
  import Stomp from 'stompjs';
  import warning from "./charts/warning";
  import smile from "./charts/emotion";
  import smile_r from "./charts/rightemotion";
  import locale from "ant-design-vue/lib/date-picker/locale/zh_CN";

  export default {
    name: "column",
    components: {
      warning,
       smile,
       smile_r,
    },

    data() {
      return {
        updateCount: 0,
        item: [],
        gaps: [2, 4, 5, 10, 20],
        nowStation: '',
        formDate: {
          gap: 2,
          station: "飞腾系列1", start: moment().subtract(1, 'hour').format("YYYY-MM-DD HH:mm:ss"),
          end: moment().format("YYYY-MM-DD HH:mm:ss")
        },
        
        moment,
        locale,
        loadTime: 0,
        startLoadTime: 0,
        endLoadTime: 0 ,
        receiveCount: 0,
        stompClient: '',
        chart: null,
      }
    },
    mounted() {
      this.begin();
      this.initWebSocket();
      this.initData();
      // const that = this;
      // this.$http.get("getStationsRunning").then(function (res) {
      //   that.stations = res.data.result;
      // }).catch()
    },
    methods: {
      level1(part){
        const a = document.querySelector(part);
        a.className='';
        a.classList.add('level1');
      },
      level2(part){
        const a = document.querySelector(part);
        a.className='';
        a.classList.add('level2');
      },
      level3(part){
        const a = document.querySelector(part);
        a.className='';
        a.classList.add('level3');
      },
      level0(part){
        const a = document.querySelector(part);
        a.className='';
        a.classList.add('level0');
      },

      begin() {
        this.items = document.querySelectorAll('.container .item')
        for (let i = 0; i < this.items.length; i++) {
          this.items[i].order = i;
          const start = new Date()
          this.startLoadTime= start.getTime()
        }
      },
      duringTime(time) {
        const showTime = new Date();
        const occurTime = new Date(time);
        return showTime.getTime() - occurTime.getTime() + '毫秒'
      },
      handleSubmit() {
        const start = new Date()
        this.startLoadTime= start.getTime()
        this.loadTime=0;
        this.$message.success("修改成功！");
        //把数据置成空！
      },
      initData() {

      },
      initWebSocket() {
        this.connection();
        let that = this;
        // 断开重连机制,尝试发送消息,捕获异常发生时重连
        this.timer = setInterval(() => {
          try {
            that.stompClient.send("秦少乾11");
          } catch (err) {
            console.log("断线了: " + err);
            that.connection();
          }
        }, 5000);
      },
      connection() {
        const that = this;
        // 建立连接对象
        let socket = new SockJS('http://127.0.0.1:8887/ws');
        // 获取STOMP子协议的客户端对象
        this.stompClient = Stomp.over(socket);
        // 定义客户端的认证信息,按需求配置
        let headers = {
          Authorization: ''
        }
        // 向服务器发起websocket连接
        this.stompClient.connect(headers, () => {
          this.stompClient.subscribe('/topic/public', (msg) => { // 订阅服务端提供的某个topic
            const message = JSON.parse(msg.body);
            that.changeValue(message);
            if(this.receiveCount===0){
              const end = new Date()
              this.endLoadTime = end.getTime()
              this.loadTime = this.endLoadTime-this.startLoadTime
              this.receiveCount = 1
            }
          }, headers);
          this.stompClient.send("成功了",
              headers,
              JSON.stringify({sender: '', chatType: 'JOIN'}),
          )
          //用户加入接口
        }, (err) => {
          // 连接发生错误时的处理函数
          console.log('失败')
          console.log(err);
        });
      },    //连接 后台
      disconnect() {
        if (this.stompClient) {
          this.stompClient.disconnect();
        }
      },  // 断开连接
      changeValue(res) {

        const that = this;
        if (res.heatExchangeStation.toString() !== that.formDate.station) {
          return;
        }

        const time = moment(res.time).format("YYYY-MM-DD HH:mm:ss");

        that.updateCount++;

        if (res.onceTemperature > 55) {
          var warning2 = {
            proName: '芯片工作电压过高',
            proData: res.onceTemperature.toFixed(2),
            time: time,
            proReason: '未知',
            proState: '0',
            showTime: this.duringTime(time),
            finTime: -1
          }
          that.$refs.g1.tableData.push(warning2);
          const xg1 = res.onceTemperature - 55;
          if(xg1>5){
             this.$refs.smile1.A1alarm3();
            this.level3('#one');
          }else if(xg1>3&&xg1<=5){
             this.$refs.smile1.A1alarm2();
            this.level2('#one');
          }else{
             this.$refs.smile1.A1alarm1();
            this.level1('#one');
          }
        }

        if (res.instantaneousTraffic > 83) {
          var warning5 = {
            proName: '芯片电流过高',
            proData: res.instantaneousTraffic.toFixed(2),
            time: time,
            proReason: '未知',
            proState: '0',
            showTime: this.duringTime(time),
            finTime: -1
          };
          that.$refs.g2.tableData.push(warning5);
          const xg2 = res.instantaneousTraffic - 83;
          if(xg2>5){
             this.$refs.smile2.A2alarm3();
            this.level3('#three');
          }else if(xg2>3&&xg2<=5){
             this.$refs.smile2.A2alarm2();
            this.level2('#three');
          }else{
             this.$refs.smile2.A2alarm1();
            this.level1('#three');
          }
        }

        if (res.oneSupplyPressure > 0.75) {

          var warning41 = {
            proName: '芯片频率1过高',
            proData: res.oneSupplyPressure.toFixed(2),
            time: time,
            proReason: '未知',
            proState: '0',
            showTime: this.duringTime(time),
            finTime: -1
          };
          that.$refs.g3.tableData.push(warning41);
          const xg3 = res.oneSupplyPressure - 0.75;
          if(xg3>0.6){
             this.$refs.smile3.A3alarm3();
            this.level3('#two');
          }else if(xg3>0.3&&xg3<=0.6){
             this.$refs.smile3.A3alarm2();
            this.level2('#two');
          }else{
             this.$refs.smile3.A3alarm1();
            this.level1('#two');
          }
        }

        if (res.pressureOnce > 0.52) {
          var warning42 = {
            proName: '芯片频率2过高',
            proData: res.pressureOnce.toFixed(2),
            time: time,
            proReason: '未知',
            proState: '0',
            showTime: this.duringTime(time),
            finTime: -1
          };
          that.$refs.g4.tableData.push(warning42);

          if(res.pressureOnce > 0.55){
             this.$refs.smile4.A4alarm3();
            this.level3('#four');
          }else if(res.pressureOnce > 0.52&&res.pressureOnce <=0.55){
             this.$refs.smile4.A4alarm2();
            this.level2('#four');
          }else{
             this.$refs.smile4.A4alarm1();
            this.level1('#four');
          }

        }

        if (that.$refs.g1.tableData.length===0){
            this.$refs.smile1.happy1();
          this.level0('#one');
        }
        if (that.$refs.g2.tableData.length===0){
           this.$refs.smile2.happy2();
          this.level0('#three');
        }
        if (that.$refs.g3.tableData.length===0){
           this.$refs.smile3.happy3();
          this.level0('#two');
        }
        if (that.$refs.g4.tableData.length===0){
           this.$refs.smile4.happy4();
          this.level0('#four');
        }

      },

      beforeDestroy: function () {
        // 页面离开时断开连接,清除定时器
        this.disconnect();
        clearInterval(this.timer);
      }
    },

  }

</script>

<style>
.e{
  transform: translate(-14%,-20%) scale(0.5)
}
.f{
  transform: translate(-14%,42%) scale(0.5)
}
.g{
  transform: translate(80%, -20%) scale(0.5)
}
.h{
  transform: translate(80%, 42%) scale(0.5)
}
.item-o:hover{
  transform: translate(30%,10%) scale(0.9);
  z-index: 20;
}
  .container-o {
    position: relative;
    height: 1000px;
    width: 95%;
    overflow: hidden;
    margin: 20px auto 0;
    box-sizing: content-box;
  }

  .item-o {
    padding: 0;
    margin: 0;
    width: 60%;
    height: 80%;
    position: absolute;
    text-align: center;
    background-color: #fff;
    /*background: rgba(248, 248, 252, 0.5);*/
    border-left: solid 1px #d2d0d0;
    border-top: solid 1px #d2d0d0;
    box-shadow: 5px 5px 5px #888;
    transition: all 0.8s;
  }

.warnName{
  float: left;
  font-size: 40px;
  color:#00a0e9;
  font-weight: bold;
}
#one{
  position: absolute;
  bottom:50.7%;
  right: 50.3%;
  border-radius: 100% 0 0 0;
  transition-duration: 0.8s;
}
#two{
  position: absolute;
  bottom:50.7%;
  left: 50%;
  border-radius: 0 100% 0 0;
  transition-duration: 0.8s;
}
#three{
  position: absolute;
  top:50%;
  right: 50.3%;
  border-radius: 0 0 0 100%;
  transition-duration: 0.8s;
}
#four{
  position: absolute;
  top:50%;
  left: 50%;
  border-radius: 0 0 100% 0;
  transition-duration: 0.8s;

}
.level0{
  height: 100px;
  width: 100px;
  background-color: #1dad00;
}
.level1{
  height: 120px;
  width: 120px;
  background-color: rgb(236, 236, 10);
}
.level2{
  height: 140px;
  width: 140px;
  background-color: orange;
}
.level3{
  height: 160px;
  width: 160px;
  background-color: red;
}
</style>
