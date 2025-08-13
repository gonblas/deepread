import { BookOpen, ArrowLeft } from "lucide-react";
import { Button } from "@/components/ui/button";
import { motion } from "framer-motion";
import { useNavigate } from "react-router-dom";

export function ChapterNotFound() {
  const navigate = useNavigate();
  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 flex items-center justify-center">
      <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="text-center">
        <BookOpen className="w-16 h-16 text-muted-foreground mx-auto mb-4" />
        <h1 className="text-2xl font-bold mb-2">Chapter Not Found</h1>
        <p className="text-muted-foreground mb-6">
          The chapter you're looking for doesn't exist.
        </p>
        <Button onClick={() => navigate("/books/")} variant="outline">
          <ArrowLeft className="w-4 h-4 mr-2" />
          Back to Books
        </Button>
      </motion.div>
    </div>
  );
}
