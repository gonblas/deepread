import { Skeleton } from "../ui/skeleton";

export function ChapterSkeleton() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20">
        {/* Loading Header */}
        <div className="sticky top-0 z-50 backdrop-blur-xl bg-background/80 border-b border-border/50">
          <div className="max-w-5xl mx-auto px-6 py-4">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-4">
                <Skeleton className="h-6 w-20" />
                <div className="flex gap-6">
                  <Skeleton className="h-4 w-24" />
                  <Skeleton className="h-4 w-20" />
                  <Skeleton className="h-4 w-16" />
                </div>
              </div>
              <div className="flex gap-2">
                <Skeleton className="h-9 w-16" />
                <Skeleton className="h-9 w-9" />
              </div>
            </div>
          </div>
        </div>

        {/* Loading Content */}
        <div className="max-w-5xl mx-auto px-6 py-12">
          <div className="mb-16">
            <Skeleton className="h-16 w-3/4 mb-4" />
          </div>
          <div className="space-y-4">
            <Skeleton className="h-4 w-full" />
            <Skeleton className="h-4 w-5/6" />
            <Skeleton className="h-4 w-4/5" />
            <Skeleton className="h-120 w-full mt-8" />
          </div>
        </div>
      </div>
  );
}