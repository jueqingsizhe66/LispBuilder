class LispBuilder {
  private LispList readBuffer

  def readStart() {
    readBuffer = null
  }

  def readResult() {
    readBuffer
  }

  def build(Closure c) {
    readStart()
    c.delegate = this
    c.resolveStrategy = Closure.DELEGATE_FIRST;
    c.call()
    return readResult()
  }

  def invokeMethod(String m, args) {
    try {
      if (m != '$') {
        getProperty(m)
      }
      def elem = args[0]
      if (elem instanceof Closure) {
        LispBuilder reader = new LispBuilder()
        elem = reader.build(elem)
      }
      if (elem != null) {
        if (readBuffer == null) {
          readBuffer = [elem] as LispList
        }
        else {
          readBuffer.append_(elem)
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace()
    }
  }

  def getProperty(String p) {
    try {
      def value = p
      if (p != '$if' && p.startsWith('$')) {
        value = Integer.parseInt(p.substring(1,p.size()))
      }
      else {
        value = new String(value) // 新しいインスタンスを作ることは必須(intern対策)
        value.metaClass.getIsSymbol = { true } // インスタンスごとメタクラス設定
      }
      if (readBuffer == null) {
        readBuffer = [value] as LispList
      }
      else {
        readBuffer.append_(value)
      }
    }
    catch (Exception e) {
      e.printStackTrace()
    }
  }

}