
import java.io.File

import avrohugger._
import avrohugger.format.SpecificRecord
import org.specs2._

class CommentsSpec extends mutable.Specification {

  "a Generator" should {

    "use the comments from idl files in their Scala counterparts." in {
      val infile = new java.io.File("avrohugger-core/src/test/avro/comments.avdl")
      val gen = new Generator(SpecificRecord)
      val outDir = gen.defaultOutputDir + "/specific/"
      gen.fileToFile(infile, outDir)

      val sourceRecord = scala.io.Source.fromFile(s"$outDir/com/example/NoSpaces.scala").mkString
      sourceRecord ====
        """/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
          |package com.example
          |
          |/**
          | * The comment applies to the `NoSpaces` record, but is not indented to the
          | * level of the record specification.
          | * @param single_line_comment_property This is a single line comment that is indented for readability,
          | *     and is not affected by indentation.
          | * @param multi_line_property This multi-line comment on `mult_line_property` that would be affected by indentation.
          | * 
          | * This is another paragraph
          | * 
          | * 
          | *     This is an indented block and should be shown as
          | *     such.
          | * 
          | * Here is a code block that apparently does not work for avrodoc. E.g. no [GFM](https://help.github.com/articles/github-flavored-markdown) support.
          | * 
          | * ```ruby
          | * # this is a Ruby code block
          | * def method(arg1, arg2=nil)
          | *   puts "hello world!"
          | * end
          | * ```
          | */
          |case class NoSpaces(var single_line_comment_property: String, var multi_line_property: String) extends org.apache.avro.specific.SpecificRecordBase {
          |  def this() = this("", "")
          |  def get(field: Int): AnyRef = {
          |    field match {
          |      case pos if pos == 0 => {
          |        single_line_comment_property
          |      }.asInstanceOf[AnyRef]
          |      case pos if pos == 1 => {
          |        multi_line_property
          |      }.asInstanceOf[AnyRef]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |  }
          |  def put(field: Int, value: Any): Unit = {
          |    field match {
          |      case pos if pos == 0 => this.single_line_comment_property = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case pos if pos == 1 => this.multi_line_property = {
          |        value.toString
          |      }.asInstanceOf[String]
          |      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
          |    }
          |    ()
          |  }
          |  def getSchema: org.apache.avro.Schema = NoSpaces.SCHEMA$
          |}
          |
          |object NoSpaces {
          |  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"NoSpaces\",\"namespace\":\"com.example\",\"doc\":\"The comment applies to the `NoSpaces` record, but is not indented to the\\nlevel of the record specification.\",\"fields\":[{\"name\":\"single_line_comment_property\",\"type\":\"string\",\"doc\":\"This is a single line comment that is indented for readability,\\n    and is not affected by indentation.\"},{\"name\":\"multi_line_property\",\"type\":\"string\",\"doc\":\"This multi-line comment on `mult_line_property` that would be affected by indentation.\\n\\nThis is another paragraph\\n\\n\\n    This is an indented block and should be shown as\\n    such.\\n\\nHere is a code block that apparently does not work for avrodoc. E.g. no [GFM](https://help.github.com/articles/github-flavored-markdown) support.\\n\\n```ruby\\n# this is a Ruby code block\\ndef method(arg1, arg2=nil)\\n  puts \\\"hello world!\\\"\\nend\\n```\"}]}")
          |}
          |""".stripMargin.trim
    }


  }

}
