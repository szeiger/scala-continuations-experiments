import sbt._

class ScalaContinuationsExperimentsProject(info: ProjectInfo) extends DefaultProject(info) with AutoCompilerPlugins
{
  val cont = compilerPlugin("org.scala-lang.plugins" % "continuations" % "2.8.0")
  override def compileOptions = super.compileOptions ++ compileOptions("-P:continuations:enable")
}
