"use client"

import { Badge } from "@/components/ui/badge"

type QuestionOption = {
  id: string
  text: string
  isCorrect: boolean
}

type Question = {
  id: string
  type: "MULTIPLE_CHOICE" | "TRUE_FALSE" | "OPEN"
  prompt: string
  explanation?: string
  options?: QuestionOption[]
  correctAnswerIds?: string[]
  isAnswerTrue?: boolean
  expectedAnswerText?: string
  trueFalseText?: string
}

type UserAnswer = {
  optionIds?: string[]
  answerText?: string
  isTrue?: boolean
}

type QuestionDisplayProps = {
  question: Question
  index: number
  userAnswer?: UserAnswer
  mode: "results" | "preview"
}

// ====================== Multiple Choice ======================
function MultipleChoiceQuestion({
  question,
  userAnswer,
  mode,
}: {
  question: Question
  userAnswer?: UserAnswer
  mode: "results" | "preview"
}) {
  if (!question.options) return null

  return (
    <div className="space-y-3 mt-2">
      {question.options.map((option, idx) => {
        const isCorrect = option.isCorrect
        const isSelected = mode === "results" && userAnswer?.optionIds?.includes(option.id)

        // Colores según estado
        let borderColor = "border-border"
        let bgColor = "bg-background"
        let textColor = "text-foreground"
        let letterBg = "bg-muted text-muted-foreground"

        if (mode === "results") {
          if (isCorrect) {
            borderColor = "border-green-500"
            bgColor = "bg-green-50 dark:bg-green-950/30"
            textColor = "text-green-800 dark:text-green-200"
            letterBg = "bg-green-600 text-white"
          }
          if (isSelected && !isCorrect) {
            borderColor = "border-red-500"
            bgColor = "bg-red-50 dark:bg-red-950/30"
            textColor = "text-red-800 dark:text-red-200"
            letterBg = "bg-red-600 text-white"
          }
        }

        return (
          <div
            key={option.id}
            className={`flex items-start gap-3 p-4 rounded-lg border-2 ${borderColor} ${bgColor}`}
          >
            <div
              className={`size-8 rounded-full flex items-center justify-center text-sm font-medium ${letterBg}`}
            >
              {String.fromCharCode(65 + idx)}
            </div>

            <div className="flex-1 flex flex-col">
              <div className="flex items-center gap-2 flex-wrap justify-between mt-1">
                <p className={`font-medium ${textColor}`}>{option.text}</p>
                {mode === "results" && (
                  <>
                    {isSelected && !isCorrect && (
                      <Badge
                        variant="outline"
                        className="text-xs font-medium px-2 py-1 border-red-500 text-red-700"
                      >
                        Your Answer
                      </Badge>
                    )}
                    {isCorrect && (
                      <Badge
                        variant="outline"
                        className="text-xs font-medium px-2 py-1 border-green-500 text-green-700 ml-auto"
                      >
                        {isSelected ? "Your Answer" : "Correct Answer"}
                      </Badge>
                    )}
                  </>
                )}
              </div>
            </div>
          </div>
        )
      })}
    </div>
  )
}





// ====================== True/False ======================
function TrueFalseQuestion({
  question,
  userAnswer,
  mode,
}: {
  question: Question
  userAnswer?: UserAnswer
  mode: "results" | "preview"
}) {
  return (
    <div className="mt-2 space-y-2">
      <p className="font-medium">
        Correct answer: <span className="text-green-600">{question.trueFalseText}</span>
      </p>
      {mode === "results" && userAnswer && (
        <p>
          Your answer:{" "}
          <span className={userAnswer.isTrue === question.isAnswerTrue ? "text-green-600" : "text-red-600"}>
            {userAnswer.isTrue ? "True" : "False"}
          </span>
        </p>
      )}
    </div>
  )
}

// ====================== Open Question ======================
function OpenQuestion({
  question,
  userAnswer,
  mode,
}: {
  question: Question
  userAnswer?: UserAnswer
  mode: "results" | "preview"
}) {
  return (
    <div className="mt-2 space-y-2">
      <p className="font-medium">
        Correct answer: <span className="text-green-600">{question.expectedAnswerText}</span>
      </p>
      {mode === "results" && userAnswer?.answerText && (
        <p>
          Your answer:{" "}
          <span
            className={
              userAnswer.answerText.trim().toLowerCase() ===
              question.expectedAnswerText?.trim().toLowerCase()
                ? "text-green-600"
                : "text-red-600"
            }
          >
            {userAnswer.answerText}
          </span>
        </p>
      )}
    </div>
  )
}

// ====================== Main QuestionDisplay ======================
export function QuestionDisplay({ question, index, userAnswer, mode }: QuestionDisplayProps) {
  return (
    <div className="space-y-4 p-6 border rounded-xl bg-card shadow-sm">
      {/* Número + Prompt */}
      <div className="flex items-center gap-4">
        <Badge variant="outline" className="mb-auto mt-1">
          {index + 1}
        </Badge>
        <h3 className="text-lg font-semibold">{question.prompt}</h3>
      </div>

      {/* Render según tipo */}
      {question.type === "MULTIPLE_CHOICE" && (
        <MultipleChoiceQuestion question={question} userAnswer={userAnswer} mode={mode} />
      )}

      {question.type === "TRUE_FALSE" && (
        <TrueFalseQuestion question={question} userAnswer={userAnswer} mode={mode} />
      )}

      {question.type === "OPEN" && <OpenQuestion question={question} userAnswer={userAnswer} mode={mode} />}

      {/* Explicación */}
      {question.explanation && (
        <div className="mt-4 p-3 rounded bg-muted text-sm text-muted-foreground">{question.explanation}</div>
      )}
    </div>
  )
}
