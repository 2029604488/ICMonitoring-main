<template>
  <div style="height: 100%; width: 98%; margin: 0 auto; position: relative">
    <div v-if="tableData.length != 0">
      <a-row :gutter="[16,16]">
          <p class="warnText">
              <img :src="require('@/assets/alarm.png')" />报警信息
              </p>
          <a-button style="position: absolute; right: 70px; top:25px" type="primary" @click="clearConfirm()">清除已解决</a-button>
      </a-row>
      <a-table :columns="columns" :data-source="tableData" :pagination="{pageSize: 6}">
        <div slot="op" slot-scope="op">
          <a-space>
            <a-button type="primary" @click="proSolve(op)">解决</a-button>
            <a-button type="default" @click="editInfo(op)">编辑</a-button>
          </a-space>
        </div>
         <span slot="proState" slot-scope="proState">
      <a-tag
          :color="proState === '1' ? 'green' : 'red' ">
        {{ proState === '1' ? '已解决' : '未解决' }}
      </a-tag>
    </span>
      </a-table>
    </div>
    <div v-else class="fine">
        <img :src="require('@/assets/normal.png')" />正常
        </div>

    <a-modal
        title="修改信息"
        :visible="visible"
        okText="修改"
        cancelText="取消"
        :confirm-loading="confirmLoading"
        @ok="handleOk()"
        @cancel="handleCancel()">
      <a-form-model :layout="form.layout" :model="edit">
        <a-form-model-item label="原因" prop="proReason" required>
          <a-input v-model="edit.proReason" placeholder="请输入原因"/>
        </a-form-model-item>
        <a-form-model-item label="状态" prop="proState" >
          <a-checkbox id="CB">撤销解决</a-checkbox>
        </a-form-model-item>
      </a-form-model>
    </a-modal>

  </div>
</template>

<script>
const columns = [
  {
    title: '报警名称',
    dataIndex: 'proName',
    key: 'proName',
  },
  {
    title: '报警数据',
    dataIndex: 'proData',
    key: 'proData',
  },
  {
    title: '发生时间',
    key: 'time',
    dataIndex: 'time',
  },
  {
    title: '原因',
    key: 'proReason',
    dataIndex: 'proReason',
  },
  {
    title: '状态',
    key: 'proState',
    dataIndex: 'proState',
    scopedSlots: {customRender: 'proState'},
     filters: [
            {
                text: "已解决",
                value: "1",
            },
            {
                text: "待解决",
                value: "0",
            },
        ],
        onFilter: (value, record) => record.proState.indexOf(value) === 0,
  },
  {
    title: '操作',
    scopedSlots: {customRender: 'op'},
  },
];
export default {
  name: "warning",
  data() {
    return {
      visible: false,
      proVisible: false,
      stompClient: '',
      tableData: [],
      edit: {},
      selected: {},
      form: {},
      rules: {},
      columns,
    };
  },
  mounted() {
  },
  methods:{
    proSolve(pro){
      const that = this;
      for (var i = 0; i <= this.tableData.length; i++) {
        if (that.tableData[i] === pro) {
          this.tableData[i].proState = '1';
        }
      }
      that.proVisible = true;
    },
    editInfo(pro){
      this.visible = true;
      this.selected = pro;
      this.edit = pro;
    },
    handleCancel(){
      this.visible = false;
    },
    handleOk(){
      if(document.getElementById('CB').checked){
        this.edit.proState = '0';
      }
      this.visible = false;
    },
    
    clearConfirm(){
      var that = this;
      const solvedTable = [];
      const unsolvedTable = [];
      for (var i = 0; i < that.tableData.length; i++) {
        if (that.tableData[i].proState === '1') {
          solvedTable.push(that.tableData[i]);
        }else{
          unsolvedTable.push(that.tableData[i]);
        }
      }
      if(solvedTable.length === 0){
        alert('没有问题被解决');  
        return
      }
      //可以加推送后端
      // var pushForm = {solvedProblems:[],info:null};
      // pushForm.solvedProblems = solvedTable;
      // pushForm.info = this.solvedInfo.info;
      that.tableData = unsolvedTable;
    },
   
  }
}
</script>

<style scoped>
.fine{
  font-size: 120px;
  margin-top: 160px;
  color: #2bbd03;
}
.warnText{
  font-size: 30px;
  margin-top: 20px;
  margin-left: 10px;
  margin-bottom: 10px;
  color: red;
}

</style>
