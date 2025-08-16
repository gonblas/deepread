import { Clock } from "lucide-react";
import { formatTime } from "@/lib/formatTime";
import { Card, CardContent } from "./ui/card";


export function ClockCard({ time }: { time: number }) {
  return (
    <Card className="shrink-0 py-3">
      <CardContent>
        <div className="flex items-center gap-2">
          <Clock className="size-5 text-orange-500" />
          <div>
            <p className="text-2xl font-bold font-mono">
              {formatTime(time)}
            </p>
            <p className="text-xs text-muted-foreground">Elapsed Time</p>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
