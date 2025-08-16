"use client"

import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { CheckCircle, Circle } from "lucide-react"
import type { Question } from "@/contexts/quizContext"

interface QuestionDisplayProps {
  question: Question
  index: number
  mode: "preview" | "results"
  userAnswer?: {
    type: "MULTIPLE_CHOICE" | "TRUE_FALSE" | "OPEN"
    optionIds?: string[]
    answer?: boolean
    answerText?: string
  }
  isCorrect?: boolean
}

export function QuestionDisplay({ question, index, mode, userAnswer, isCorrect }: QuestionDisplayProps) {
  const getQuestionTypeLabel = (type: Question["type"]) => {
    switch (type) {
      case "MULTIPLE_CHOICE":
        return "Multiple Choice"
      case "TRUE_FALSE":
        return "True/False"
      case "OPEN":
        return "Open Ended"
      default:
        return type
    }
  }

  const getQuestionTypeBadge = (type: Question["type"]) => {
    switch (type) {
      case "MULTIPLE_CHOICE":
        return "default"
      case "TRUE_FALSE":
        return "secondary"
      case "OPEN":
        return "outline"
      default:
        return "outline"
    }
  }

  return (
    <Card
      className={
        mode === "results" && isCorrect !== undefined
          ? isCorrect
            ? "border-green-200 bg-green-50/50"
            : "border-red-200 bg-red-50/50"
          : ""
      }
    >
      <CardContent className="p-6">
        <div className="space-y-4">
          <div className="flex items-start gap-4">
            <div className="flex items-center gap-2">
              <Badge variant="outline" className="text-base px-3 py-1">
                {index + 1}
              </Badge>
              {mode === "results" &&
                isCorrect !== undefined &&
                (isCorrect ? (
                  <CheckCircle className="h-5 w-5 text-green-500" />
                ) : (
                  <Circle className="h-5 w-5 text-red-500" />
                ))}
            </div>
            <div className="flex-1">
              <h3 className="text-lg font-medium mb-2">{question.prompt}</h3>
              <div className="flex items-center gap-2">
                <Badge variant={getQuestionTypeBadge(question.type)}>{getQuestionTypeLabel(question.type)}</Badge>
                {mode === "results" && isCorrect !== undefined && (
                  <Badge variant={isCorrect ? "default" : "destructive"}>{isCorrect ? "Correct" : "Incorrect"}</Badge>
                )}
              </div>
            </div>
          </div>

          {/* Multiple Choice Options */}
          {question.type === "MULTIPLE_CHOICE" && question.options && (
            <div className="space-y-3">
              {mode === "results" && <h4 className="font-medium">Options:</h4>}
              {question.options.map((option, optionIndex) => {
                // Handle both string array and object array formats
                const optionText = typeof option === "string" ? option : option.text
                const isCorrectOption = question.correctAnswer === optionText
                const isSelected = mode === "results" && userAnswer?.optionIds?.includes(optionText)

                return (
                  <div
                    key={optionIndex}
                    className={`flex items-start gap-3 p-4 rounded-lg border-2 transition-colors ${
                      mode === "preview"
                        ? isCorrectOption
                          ? "border-green-200 bg-green-50"
                          : "border-border bg-background"
                        : isCorrectOption
                          ? "border-green-500 bg-green-50"
                          : isSelected
                            ? "border-blue-500 bg-blue-50"
                            : "border-border bg-background"
                    }`}
                  >
                    <div className="flex items-center gap-3">
                      {/* Option Letter */}
                      <div
                        className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-medium ${
                          mode === "preview"
                            ? isCorrectOption
                              ? "bg-green-500 text-white"
                              : "bg-muted text-muted-foreground"
                            : isCorrectOption
                              ? "bg-green-500 text-white"
                              : isSelected
                                ? "bg-blue-500 text-white"
                                : "bg-muted text-muted-foreground"
                        }`}
                      >
                        {String.fromCharCode(65 + optionIndex)}
                      </div>

                      {/* Selection Indicator (Results only) */}
                      {mode === "results" && isSelected && (
                        <div className="w-6 h-6 rounded-full bg-blue-500 flex items-center justify-center">
                          <div className="w-3 h-3 rounded-full bg-white"></div>
                        </div>
                      )}

                      {/* Correct Indicator */}
                      {isCorrectOption && <CheckCircle className="w-5 h-5 text-green-600" />}
                    </div>

                    <div className="flex-1">
                      <p
                        className={`${
                          mode === "preview"
                            ? isCorrectOption
                              ? "text-green-800 font-medium"
                              : "text-foreground"
                            : isCorrectOption
                              ? "text-green-800 font-medium"
                              : isSelected
                                ? "text-blue-800 font-medium"
                                : "text-foreground"
                        }`}
                      >
                        {optionText}
                      </p>
                      {mode === "results" && (
                        <div className="flex gap-2 mt-2">
                          {isSelected && (
                            <Badge variant="outline" className="text-xs border-blue-500 text-blue-700">
                              Your Answer
                            </Badge>
                          )}
                          {isCorrectOption && (
                            <Badge variant="default" className="text-xs bg-green-600 hover:bg-green-700">
                              Correct Answer
                            </Badge>
                          )}
                        </div>
                      )}
                    </div>
                  </div>
                )
              })}
            </div>
          )}

          {/* True/False Options */}
          {question.type === "TRUE_FALSE" && (
            <div className="space-y-3">
              {mode === "results" && <h4 className="font-medium">Options:</h4>}
              <div className="grid grid-cols-2 gap-4">
                {/* True Option */}
                <div
                  className={`flex items-center gap-3 p-4 rounded-lg border-2 transition-colors ${
                    mode === "preview"
                      ? question.correctAnswer === true
                        ? "border-green-200 bg-green-50"
                        : "border-border bg-background"
                      : question.correctAnswer === true
                        ? "border-green-500 bg-green-50"
                        : userAnswer?.answer === true
                          ? "border-blue-500 bg-blue-50"
                          : "border-border bg-background"
                  }`}
                >
                  <div className="flex items-center gap-3">
                    <div
                      className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-medium ${
                        mode === "preview"
                          ? question.correctAnswer === true
                            ? "bg-green-500 text-white"
                            : "bg-muted text-muted-foreground"
                          : question.correctAnswer === true
                            ? "bg-green-500 text-white"
                            : userAnswer?.answer === true
                              ? "bg-blue-500 text-white"
                              : "bg-muted text-muted-foreground"
                      }`}
                    >
                      T
                    </div>
                    {mode === "results" && userAnswer?.answer === true && (
                      <div className="w-6 h-6 rounded-full bg-blue-500 flex items-center justify-center">
                        <div className="w-3 h-3 rounded-full bg-white"></div>
                      </div>
                    )}
                    {question.correctAnswer === true && <CheckCircle className="w-5 h-5 text-green-600" />}
                  </div>
                  <div className="flex-1">
                    <p
                      className={`font-medium ${
                        mode === "preview"
                          ? question.correctAnswer === true
                            ? "text-green-800"
                            : "text-foreground"
                          : question.correctAnswer === true
                            ? "text-green-800"
                            : userAnswer?.answer === true
                              ? "text-blue-800"
                              : "text-foreground"
                      }`}
                    >
                      True
                    </p>
                    {mode === "results" && (
                      <div className="flex gap-2 mt-1">
                        {userAnswer?.answer === true && (
                          <Badge variant="outline" className="text-xs border-blue-500 text-blue-700">
                            Your Answer
                          </Badge>
                        )}
                        {question.correctAnswer === true && (
                          <Badge variant="default" className="text-xs bg-green-600 hover:bg-green-700">
                            Correct Answer
                          </Badge>
                        )}
                      </div>
                    )}
                  </div>
                </div>

                {/* False Option */}
                <div
                  className={`flex items-center gap-3 p-4 rounded-lg border-2 transition-colors ${
                    mode === "preview"
                      ? question.correctAnswer === false
                        ? "border-green-200 bg-green-50"
                        : "border-border bg-background"
                      : question.correctAnswer === false
                        ? "border-green-500 bg-green-50"
                        : userAnswer?.answer === false
                          ? "border-blue-500 bg-blue-50"
                          : "border-border bg-background"
                  }`}
                >
                  <div className="flex items-center gap-3">
                    <div
                      className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-medium ${
                        mode === "preview"
                          ? question.correctAnswer === false
                            ? "bg-green-500 text-white"
                            : "bg-muted text-muted-foreground"
                          : question.correctAnswer === false
                            ? "bg-green-500 text-white"
                            : userAnswer?.answer === false
                              ? "bg-blue-500 text-white"
                              : "bg-muted text-muted-foreground"
                      }`}
                    >
                      F
                    </div>
                    {mode === "results" && userAnswer?.answer === false && (
                      <div className="w-6 h-6 rounded-full bg-blue-500 flex items-center justify-center">
                        <div className="w-3 h-3 rounded-full bg-white"></div>
                      </div>
                    )}
                    {question.correctAnswer === false && <CheckCircle className="w-5 h-5 text-green-600" />}
                  </div>
                  <div className="flex-1">
                    <p
                      className={`font-medium ${
                        mode === "preview"
                          ? question.correctAnswer === false
                            ? "text-green-800"
                            : "text-foreground"
                          : question.correctAnswer === false
                            ? "text-green-800"
                            : userAnswer?.answer === false
                              ? "text-blue-800"
                              : "text-foreground"
                      }`}
                    >
                      False
                    </p>
                    {mode === "results" && (
                      <div className="flex gap-2 mt-1">
                        {userAnswer?.answer === false && (
                          <Badge variant="outline" className="text-xs border-blue-500 text-blue-700">
                            Your Answer
                          </Badge>
                        )}
                        {question.correctAnswer === false && (
                          <Badge variant="default" className="text-xs bg-green-600 hover:bg-green-700">
                            Correct Answer
                          </Badge>
                        )}
                      </div>
                    )}
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* Open Ended Answer */}
          {question.type === "OPEN" && (
            <div className="space-y-4">
              {mode === "results" && (
                <div>
                  <h4 className="font-medium mb-2">Your Answer:</h4>
                  <div className="p-4 bg-muted rounded-lg border">
                    <p className="text-foreground">{userAnswer?.answerText || "No answer provided"}</p>
                  </div>
                </div>
              )}
              {mode === "preview" && (
                <div className="p-4 bg-muted/50 rounded-lg border">
                  <p className="text-sm text-muted-foreground italic">
                    Open-ended question - students will type their answer
                  </p>
                </div>
              )}
              {question.correctAnswer && (
                <div>
                  <h4 className={`font-medium mb-2 ${mode === "results" ? "text-green-600" : "text-foreground"}`}>
                    {mode === "results" ? "Expected Answer:" : "Expected Answer:"}
                  </h4>
                  <div
                    className={`p-4 rounded-lg border ${mode === "results" ? "bg-green-50 border-green-200" : "bg-muted border-border"}`}
                  >
                    <p className={mode === "results" ? "text-green-800" : "text-foreground"}>
                      {question.correctAnswer}
                    </p>
                  </div>
                </div>
              )}
            </div>
          )}

          {/* Explanation */}
          {question.explanation && (
            <div>
              <h4 className="font-medium mb-2">Explanation:</h4>
              <Card className="bg-blue-50 border-blue-200">
                <CardContent className="p-4">
                  <p className="text-blue-800">{question.explanation}</p>
                </CardContent>
              </Card>
            </div>
          )}
        </div>
      </CardContent>
    </Card>
  )
}
