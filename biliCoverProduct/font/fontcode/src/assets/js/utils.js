// 自定义判断元素类型JS
function toType(obj) {
  return ({}).toString.call(obj).match(/\s([a-zA-Z]+)/)[1].toLowerCase()
}

// 参数过滤函数
function filterNull(o) {
  for (var key in o) {
    if (o[key] === null) {
      delete o[key]
    }
    if (toType(o[key]) === 'string') {
      o[key] = o[key].trim()
    } else if (toType(o[key]) === 'object') {
      o[key] = filterNull(o[key])
    } else if (toType(o[key]) === 'array') {
      o[key] = filterNull(o[key])
    }
  }
  return o
}

function blobToDataUrl(blob, callback) {
  let a = new FileReader()
  a.onload = function (e) { callback(e.target.result) }
  a.readAsDataURL(blob)
}

function dataURLtoFile(dataurl, filename) { // 将base64转换为文件
  var arr = dataurl.split(','); var mime = arr[0].match(/:(.*?);/)[1]
  var bstr = atob(arr[1]); var n = bstr.length; var u8arr = new Uint8Array(n)
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n)
  }
  var reg = /.+\/(.+)$/
  let mimeArr = mime.match(reg)
  return new File([u8arr], filename + '.' + mimeArr[1], { type: mime })
}
export {
  toType,
  filterNull,
  blobToDataUrl,
  dataURLtoFile
}
