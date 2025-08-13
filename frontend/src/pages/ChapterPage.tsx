import AppLayout from "@/components/AppLayout";
import ChapterDetails from "@/components/chapters/ChapterDetails";
import { ChapterProvider } from "@/contexts/chapterContext";

export function ChapterPage() {
  return (
    <AppLayout>
      <ChapterProvider>
        <ChapterDetails isNew={false} />
      </ChapterProvider>
    </AppLayout>
  );
}
