import { Users } from "lucide-react";

export function AuthorsList({ authors }: { authors: string[] }) {
  if (authors.length === 0) {
    return (
      <div className="flex items-center gap-1 text-sm text-muted-foreground">
        <Users className="w-3 h-3 flex-shrink-0" />
        <span className="italic">No author specified</span>
      </div>
    );
  }

  return (
    <div className="flex items-center gap-1 text-sm text-muted-foreground">
      <Users className="w-3 h-3 flex-shrink-0" />
      <span className="line-clamp-1" title={authors.join(", ")}>
        {authors.join(", ")}
      </span>
    </div>
  );
}
