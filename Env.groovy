class Env {
  Map localEnv = [:]
  def parentEnv

  Env(env = null) {
    if (env == null) {
      parentEnv = [:]
      Functions.registerFunctions(parentEnv)
    }
    else {
      this.parentEnv = env
    }
  }


  def getProperty(String key) {
    if (localEnv.containsKey(key)) {
      return localEnv[key]
    }
    if (parentEnv != null) {
      return parentEnv[key]
    }
    null
  }

  void setProperty(String key, value) {
    localEnv[key] = value
  }

  def containsKey(key) {
    if (localEnv.containsKey(key)) {
      return true
    }
    if (parentEnv.containsKey(key)) {
      return true
    }
    return false
  }

  String toString() {
    "local:" + localEnv.toString() + "parent:" + parentEnv.toString()
  }
}