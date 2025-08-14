"use client";

import { useEffect } from "react";
import { QuizProvider, useQuiz } from "@/contexts/quizContext";
import { QuizCreator } from "@/components/quizzes/QuizCreator";
import { BackButton } from "@/components/ui/back-button";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { useNavigate, useParams } from "react-router-dom";

function EditQuizContent() {
  const navigate = useNavigate();
  const { quizId } = useParams();
  const { quiz, updateQuiz, fetchQuiz, loading } = useQuiz();

  useEffect(() => {
    if (quizId) {
      fetchQuiz(quizId);
    }
  }, [quizId, fetchQuiz]);

  const handleSave = async () => {
    if (!quiz) return;

    try {
      await updateQuiz(quizId, quiz);
      navigate("/quizzes");
    } catch (error) {
      console.error("Failed to update quiz:", error);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <LoadingSpinner className="mx-auto mb-4" />
          <p className="text-muted-foreground">Loading quiz...</p>
        </div>
      </div>
    );
  }

  if (!quiz) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <p className="text-muted-foreground mb-4">Quiz not found</p>
          <BackButton href="/quizzes" label="Go back to quizzes" />
        </div>
      </div>
    );
  }

  return <QuizCreator onSave={handleSave} isEditing={true} />;
}

export default function EditQuizPage() {
  return (
    <QuizProvider>
      <EditQuizContent />
    </QuizProvider>
  );
}
