class LispBuilder3 {

  private LispList dummy = [1] as LispList

  def convertToLispList(List list) {
    if (list.size() == 0) {
      return null
    }
    def elem = list.first()
    if (elem instanceof List) {
      elem = convertToLispList(elem)
    }
    return new Cons(elem, convertToLispList(list.tail()))
  }

  def build(Closure c) {
    c.delegate = this
    c.resolveStrategy = Closure.DELEGATE_FIRST;
//    c.resolveStrategy = Closure.DELEGATE_ONLY;
    return convertToLispList(c.call())
  }

  def invokeMethod(String m, args) {
    if (m == '$') {
      if (args.length == 0) {
        return null
      }
      def result = new Cons(args[0], null)
      for (int i=1; i<args.length; i++) {
        result.append_(args[i])
      }
      return result
    }
  }

  static makeSymbol(s) {
    def result = new String(s) /* new String instance is reqired (countermasure of 'interned' equality check) */
    result.metaClass.getIsSymbol = { true } /* set closure through metaclass for instance */
    result
  }

  def getProperty(String p) {
    return makeSymbol(p)
  }

}

