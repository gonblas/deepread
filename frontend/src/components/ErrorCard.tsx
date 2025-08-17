import { Button } from "./ui/button"
import { Card, CardContent } from "./ui/card"
import { TriangleAlert } from "lucide-react"

interface ErrorCardProps {
  error?: string | null
  onRetry?: () => void
  title?: string
}

export function ErrorCard({
  error,
  onRetry,
  title = "Error"
}: ErrorCardProps) {
  return (
    <>
    {error && (
      <Card>
        <CardContent className="flex flex-col items-center justify-center py-12">
          <TriangleAlert className="h-12 w-12 text-red-600 mb-4" />
          <h3 className="text-lg font-semibold mb-2 text-red-600">{title}</h3>
          <p className="text-muted-foreground text-center mb-4">{error || "Unknown error"}</p>
          {onRetry && (
            <Button onClick={onRetry}>
              Please Try Again
            </Button>
          )}
        </CardContent>
      </Card>
    )}
    </>
  )
}
