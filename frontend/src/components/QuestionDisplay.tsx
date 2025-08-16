"use client";

import { Badge } from "@/components/ui/badge";

type QuestionOption = {
  id: string;
  text: string;
  isCorrect: boolean;
};

type Question = {
  id: string;
  type: "MULTIPLE_CHOICE" | "TRUE_FALSE" | "OPEN";
  prompt: string;
  explanation?: string;
  options?: QuestionOption[];
  correctAnswerIds?: string[];
  isAnswerTrue?: boolean;
  expectedAnswer?: string;
  trueFalseText?: string;
};

type UserAnswer = {
  optionIds?: string[];
  answerText?: string;
  isTrue?: boolean;
};

type QuestionDisplayProps = {
  question: Question;
  index: number;
  userAnswer?: UserAnswer;
  mode: "results" | "preview";
};

// ====================== Multiple Choice ======================
function MultipleChoiceQuestion({
  question,
  userAnswer,
  mode,
}: {
  question: Question;
  userAnswer?: UserAnswer;
  mode: "results" | "preview";
}) {
  if (!question.options) return null;

  return (
    <div className="space-y-3 mt-2">
      {question.options.map((option, idx) => {
        const isCorrect = option.isCorrect;
        const isSelected =
          mode === "results" && userAnswer?.optionIds?.includes(option.id);

        // Colores según estado
        let borderColor = "border-border";
        let bgColor = "bg-background";
        let textColor = "text-foreground";
        let letterBg = "bg-muted text-muted-foreground";

        if (mode === "results") {
          if (isCorrect) {
            borderColor = "border-green-500";
            bgColor = "bg-green-50 dark:bg-green-950/30";
            textColor = "text-green-800 dark:text-green-200";
            letterBg = "bg-green-600 text-white";
          }
          if (isSelected && !isCorrect) {
            borderColor = "border-red-500";
            bgColor = "bg-red-50 dark:bg-red-950/30";
            textColor = "text-red-800 dark:text-red-200";
            letterBg = "bg-red-600 text-white";
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
        );
      })}
    </div>
  );
}

// ====================== True/False ======================
function TrueFalseQuestion({
  question,
  userAnswer,
  mode,
}: {
  question: Question;
  userAnswer?: UserAnswer;
  mode: "results" | "preview";
}) {
  const options = [
    { id: "true", text: "True", isCorrect: question.isAnswerTrue },
    { id: "false", text: "False", isCorrect: question.isAnswerTrue === false },
  ];

  return (
    <div className="space-y-3 mt-2">
      {options.map((option, idx) => {
        const isCorrect = option.isCorrect;
        const isSelected =
          mode === "results" && userAnswer?.isTrue === (option.id === "true");

        // Colores según estado
        let borderColor = "border-border";
        let bgColor = "bg-background";
        let textColor = "text-foreground";
        let letterBg = "bg-muted text-muted-foreground";

        if (mode === "results") {
          if (isCorrect) {
            borderColor = "border-green-500";
            bgColor = "bg-green-50 dark:bg-green-950/30";
            textColor = "text-green-800 dark:text-green-200";
            letterBg = "bg-green-600 text-white";
          }
          if (isSelected && !isCorrect) {
            borderColor = "border-red-500";
            bgColor = "bg-red-50 dark:bg-red-950/30";
            textColor = "text-red-800 dark:text-red-200";
            letterBg = "bg-red-600 text-white";
          }
        }

        return (
          <div
            key={option.id}
            className={`flex items-start gap-3 p-4 rounded-lg border-2 ${borderColor} ${bgColor}`}
          >
            {/* Letra */}
            <div
              className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-medium ${letterBg}`}
            >
              {String.fromCharCode(65 + idx)}
            </div>

            {/* Texto y badges */}
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
        );
      })}
    </div>
  );
}

// ====================== Open Question ======================
function OpenQuestion({
  question,
  userAnswer,
  mode,
}: {
  question: Question;
  userAnswer?: UserAnswer;
  mode: "results" | "preview";
}) {
  // Determinar si la respuesta es correcta
  const isCorrect =
    userAnswer?.answerText?.trim().toLowerCase() ===
    question.expectedAnswer?.trim().toLowerCase();

  // Clases base de la caja
  const baseBoxClasses = "p-4 rounded-xl border text-sm leading-relaxed";

  // Colores según modo y corrección
  const yourAnswerClasses =
    mode === "results"
      ? isCorrect
        ? "bg-green-50 dark:bg-green-950/30 border-green-200 dark:border-green-800 text-green-900 dark:text-green-100"
        : "bg-red-50 dark:bg-red-950/30 border-red-200 dark:border-red-800 text-red-900 dark:text-red-100"
      : "bg-gray-50 dark:bg-gray-900/50 border-gray-200 dark:border-gray-700 text-gray-900 dark:text-gray-100";

  const expectedAnswerClasses =
    mode === "results"
      ? "bg-green-50 dark:bg-green-950/30 border-green-200 dark:border-green-800 text-green-900 dark:text-green-100"
      : "bg-gray-50 dark:bg-gray-900/50 border-gray-200 dark:border-gray-700 text-gray-900 dark:text-gray-100";

  return (
    <div className="space-y-4 mt-2">
      {/* Your Answer */}
      {mode === "results" && (
        <div>
          <h4 className="text-sm font-semibold text-gray-700 dark:text-gray-300 mb-2">
            Your Answer:
          </h4>
          <div className={`${baseBoxClasses} ${yourAnswerClasses}`}>
            <p>{userAnswer?.answerText?.trim()}</p>
          </div>
        </div>
      )}

      {/* Preview placeholder */}
      {mode === "preview" && (
        <div className="flex items-center justify-center p-4 bg-gray-50/50 dark:bg-gray-900/30 rounded-xl border-2 border-dashed border-gray-300 dark:border-gray-600 text-center h-32">
          <p className="text-sm text-gray-600 dark:text-gray-400 italic">
            Open-ended question - you will type your answer here
          </p>
        </div>
      )}

      {/* Expected Answer */}
      {question.expectedAnswer && (
        <div>
          <h4 className="text-sm font-semibold text-gray-700 dark:text-gray-300 mb-2">
            Expected Answer:
          </h4>
          <div className={`${baseBoxClasses} ${expectedAnswerClasses}`}>
            <p>{question.expectedAnswer}</p>
          </div>
        </div>
      )}
    </div>
  );
}

// ====================== Main QuestionDisplay ======================
export function QuestionDisplay({
  question,
  index,
  userAnswer,
  mode,
}: QuestionDisplayProps) {
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
        <MultipleChoiceQuestion
          question={question}
          userAnswer={userAnswer}
          mode={mode}
        />
      )}

      {question.type === "TRUE_FALSE" && (
        <TrueFalseQuestion
          question={question}
          userAnswer={userAnswer}
          mode={mode}
        />
      )}

      {question.type === "OPEN" && (
        <OpenQuestion question={question} userAnswer={userAnswer} mode={mode} />
      )}

      {/* Explanation */}
      {question.explanation && (
        <div className="pt-6 border-t border-gray-200 dark:border-gray-700">
          <h4 className="text-sm font-semibold text-gray-700 dark:text-gray-300 mb-3">
            Explanation:
          </h4>
          <div className="p-4 bg-blue-50/50 dark:bg-blue-950/20 rounded-xl border border-blue-200/50">
            <p className="text-sm text-gray-900 dark:text-gray-100 leading-relaxed">
              {question.explanation}
            </p>
          </div>
        </div>
      )}
    </div>
  );
}
