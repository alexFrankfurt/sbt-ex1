name := "sbt-ex1"

lazy val stTransform = taskKey[StateTransform]("Changes sbt state")
lazy val generated =  AttributeKey[Boolean]("my-settings-generated")
def generateCommandName = "generate"
def initializem = Command.command(generateCommandName)(fixState(_))

commands += initializem 

def fixState(state: State): State = 
  if(state.get(generated) getOrElse false) {
    state
  } else {
    val extracted = Project.extract(state)
    extracted.appendWithSession(Seq(), state)
    state.put(generated, true)
  }

lazy val startupTransition: State => State = { s: State =>
  "generate" :: s
}

Global / onLoad := {
    val old = (Global / onLoad).value


    startupTransition compose old
}
