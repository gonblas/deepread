import ChapterDetails from "@/components/chapters/ChapterDetails";
import { ChapterProvider } from "@/contexts/chapterContext";

export function ChapterPage() {
  return (
    <ChapterProvider>
      <ChapterDetails isNew={false} />
    </ChapterProvider>
  );
}
