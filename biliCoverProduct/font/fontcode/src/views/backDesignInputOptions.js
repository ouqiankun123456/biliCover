export default {
  text: {
    verticalText: [
      {
        value: false,
        label: '否'
      },
      {
        value: true,
        label: '是'
      }
    ], // 是否垂直文字
    fontName: [
      {
        value: '田氏颜体大字库',
        label: '田氏颜体大字库'
      },
      {
        value: '暂无，不要用',
        label: '暂无，不要用'
      }
    ], // 字体名
    fontStyle: [
      {
        value: 0,
        label: '正常'
      },
      {
        value: 1,
        label: '加粗'
      },
      {
        value: 2,
        label: '斜体'
      }
    ], // 字体样式
    horizontalAlign: [
      {
        value: 'left',
        label: '左对齐'
      },
      {
        value: 'right',
        label: '右对齐'
      },
      {
        value: 'center',
        label: '居中'
      }
    ], // 水平对齐，left:左对齐 right:右对齐 center: 居中
    verticalAlign: [
      {
        value: 'top',
        label: '上对齐'
      },
      {
        value: 'bottom',
        label: '下对齐'
      },
      {
        value: 'center',
        label: '居中'
      }
    ] // 垂直对齐, top:上对齐 bottom:下对齐 center: 居中
  },
  generalPath: {
    type: [
      {
        value: 'moveTo',
        label: '移动到某点'
      },
      {
        value: 'lineTo',
        label: '连线到某点'
      },
      {
        value: 'quadTo',
        label: '二次曲线片段'
      },
      {
        value: 'curveTo',
        label: '三次曲线片段'
      },
      {
        value: 'closePath',
        label: '闭合遮罩'
      },
      {
        value: 'reset',
        label: '重置遮罩，恢复整个页面'
      }
    ] // 根据操作类型来进行不同的方法 moveTo:移动到某点 lineTo:连线到某点 quadTo: 二次曲线片段 curveTo：三次曲线片段 closePath：闭合遮罩 reset: 重置遮罩，恢复整个页面
  }
}
