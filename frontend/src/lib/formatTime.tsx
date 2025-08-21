export function formatTime(seconds: number): string {
  seconds = Math.round(seconds);

  const hours = Math.floor(seconds / 3600);

  if (hours > 0) {
    seconds %= 3600;
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${hours.toString().padStart(2, "0")}:${mins.toString().padStart(2, "0")}:${secs.toString().padStart(2, "0")}`;
  }

  const mins = Math.floor(seconds / 60);
  const secs = seconds % 60;
  return `${mins.toString().padStart(2, "0")}:${secs.toString().padStart(2, "0")}`;
}

