export default {
  image: {
    imagePath: 'image', // 图片路径
    x: 'inputNumber', // 坐标x
    y: 'inputNumber', // 坐标y
    width: 'inputNumber', // 宽度
    height: 'inputNumber' // 高度
  },
  text: {
    verticalText: 'select', // 是否垂直文字
    fontName: 'select', // 字体名
    fontStyle: 'select', // 字体样式
    fontSize: 'inputNumber', // 字体尺寸
    textColor: 'color', // 字体颜色，格式例子rgba(19, 206, 102, 0.8)
    borderColor: 'color', // 描边颜色，格式例子rgba(19, 206, 102, 0.8)
    borderWidth: 'inputNumber', // 描边宽度
    positionX: 'inputNumber', // 文本框x坐标
    positionY: 'inputNumber', // 文本框y坐标
    textAreaWidth: 'inputNumber', // 文本框宽度，用于计算对齐
    textAreaHeight: 'inputNumber', // 文本框高度，用于计算对齐
    horizontalAlign: 'select', // 水平对齐，left:左对齐 right:右对齐 center: 居中
    verticalAlign: 'select', // 垂直对齐, top:上对齐 bottom:下对齐 center: 居中
    text: 'input', // 文字内容
    gradientPaintX1: 'inputNumber', // 渐变点1X坐标，如果不需要渐变则为null
    gradientPaintY1: 'inputNumber', // 渐变点1Y坐标，如果不需要渐变则为null
    gradientPaintColor1: 'color', // 渐变点1颜色，如果不需要渐变则为null
    gradientPaintX2: 'inputNumber', // 渐变点2X坐标，如果不需要渐变则为null
    gradientPaintY2: 'inputNumber', // 渐变点2Y坐标，如果不需要渐变则为null
    gradientPaintColor2: 'color' // 渐变点2颜色，如果不需要渐变则为null
  },
  generalPath: {
    type: 'select', // 根据操作类型来进行不同的方法 moveTo:移动到某点 lineTo:连线到某点 quadTo: 二次曲线片段 curveTo：三次曲线片段 closePath：闭合遮罩 reset: 重置遮罩，恢复整个页面
    pointX: 'inputNumber', // 点的X坐标
    pointY: 'inputNumber', // 点的Y坐标
    bezierX1: 'inputNumber', // 二次曲线片段扭曲点X
    bezierY1: 'inputNumber', // 二次曲线片段扭曲点Y
    bezierX2: 'inputNumber', // 三次曲线片段扭曲点X
    bezierY2: 'inputNumber'// 三次曲线片段扭曲点Y
  },
  rotate: {
    x: 'inputNumber', // 围绕x坐标旋转
    y: 'inputNumber', // 围绕y坐标旋转
    theta: 'inputNumber' // 旋转角度，可负数
  }
}
