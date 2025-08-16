import { Clock } from "lucide-react";
import { useCallback } from "react";
import { Card, CardContent } from "./ui/card";


export function ClockCard({ time }: { time: number }) {
  const formatTime = useCallback((milliseconds: number): string => {
    const totalSeconds = Math.floor(milliseconds / 1000);
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;
    return `${minutes.toString().padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`;
  }, []);

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
