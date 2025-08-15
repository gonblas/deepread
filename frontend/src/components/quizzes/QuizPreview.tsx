"use client"

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Separator } from "@/components/ui/separator"
import { CheckCircle, Eye, HelpCircle } from "lucide-react"
import { EmptyState } from "@/components/ui/empty-state"
import type { Question, MultipleChoiceQuestion, TrueFalseQuestion, OpenQuestion } from "@/contexts/quizContext"

interface QuizPreviewProps {
  questions: Question[]
  onAddQuestions: () => void
}

export function QuizPreview({ questions, onAddQuestions }: QuizPreviewProps) {
  return (
    <Card>
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Eye className="w-5 h-5" />
          Quiz Preview
        </CardTitle>
        <CardDescription>See how your quiz will appear to you</CardDescription>
      </CardHeader>
      <CardContent className="space-y-6">
        {questions.length === 0 ? (
          <EmptyState
            icon={<HelpCircle className="w-12 h-12" />}
            title="No questions to preview"
            description="Add some questions to see the preview"
            action={{
              label: "Add Questions",
              onClick: onAddQuestions,
            }}
          />
        ) : (
          <div className="space-y-6">
            <div className="flex gap-4 text-sm text-muted-foreground">
              <span>{questions.length} questions</span>
            </div>

            <Separator />

            <div className="space-y-6">
              {questions.map((question, index) => (
                <div key={question.id} className="space-y-3">
                  <div className="flex items-start gap-3">
                    <span className="font-medium text-sm text-muted-foreground mt-1">{index + 1}.</span>
                    <div className="flex-1">
                      <p className="font-medium mb-2">{question.prompt}</p>

                      {question.type === "MULTIPLE_CHOICE" && (
                        <div className="space-y-2">
                          {(question as MultipleChoiceQuestion).options.map((option, optIndex) => (
                            <div key={optIndex} className="flex items-center gap-2">
                              <div className="w-4 h-4 border border-border rounded-full" />
                              <span className={option.isCorrect ? "font-medium text-green-700" : ""}>
                                {option.text}
                              </span>
                              {option.isCorrect && <CheckCircle className="w-4 h-4 text-green-600" />}
                            </div>
                          ))}
                        </div>
                      )}

                      {question.type === "TRUE_FALSE" && (
                        <div className="space-y-2">
                          <div className="flex items-center gap-2">
                            <div className="w-4 h-4 border border-border rounded-full" />
                            <span
                              className={
                                (question as TrueFalseQuestion).isAnswerTrue ? "font-medium text-green-700" : ""
                              }
                            >
                              True
                            </span>
                            {(question as TrueFalseQuestion).isAnswerTrue && (
                              <CheckCircle className="w-4 h-4 text-green-600" />
                            )}
                          </div>
                          <div className="flex items-center gap-2">
                            <div className="w-4 h-4 border border-border rounded-full" />
                            <span
                              className={
                                !(question as TrueFalseQuestion).isAnswerTrue ? "font-medium text-green-700" : ""
                              }
                            >
                              False
                            </span>
                            {!(question as TrueFalseQuestion).isAnswerTrue && (
                              <CheckCircle className="w-4 h-4 text-green-600" />
                            )}
                          </div>
                        </div>
                      )}

                      {question.type === "OPEN" && (
                        <div className="space-y-2">
                          <div className="border border-border rounded-md p-3 bg-muted/50">
                            <p className="text-sm text-muted-foreground">Open text answer</p>
                          </div>
                          <p className="text-sm text-green-700">
                            Expected: {(question as OpenQuestion).expectedAnswer}
                          </p>
                        </div>
                      )}

                      {question.explanation && (
                        <div className="mt-3 p-3 bg-card border border-border rounded-md">
                          <p className="text-sm text-card-foreground">
                            <strong>Explanation:</strong> {question.explanation}
                          </p>
                        </div>
                      )}
                    </div>
                  </div>
                  {index < questions.length - 1 && <Separator />}
                </div>
              ))}
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  )
}
