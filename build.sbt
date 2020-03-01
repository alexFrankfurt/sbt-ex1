name := "sbt-ex1"

lazy val stTransform = taskKey[StateTransform]("Changes sbt state")

stTransform := {
    val s = state.value
    val extracted = Project.extract(s)

    val nwSt = extracted.appendWithSession(Seq(), s)

    new StateTransform(nwSt)
}

lazy val startupTransition: State => State = { s: State =>
  "stTransform" :: s
}

Global / onLoad := {
    val old = (Global / onLoad).value


    startupTransition compose old
}
