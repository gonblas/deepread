import { Button } from "./ui/button"
import { Card, CardContent } from "./ui/card"

interface ErrorCardProps {
  error: string
  onRetry?: () => void
  title?: string
  retryButtonText?: string
}

export function ErrorCard({
  error,
  onRetry,
  title = "Error",
  retryButtonText = "Please Try Again",
}: ErrorCardProps) {
  return (
    <>
    {error && (
      <Card>
        <CardContent className="flex flex-col items-center justify-center py-12">
          <div className="text-red-500 mb-4 text-4xl select-none">⚠️</div>
          <h3 className="text-lg font-semibold mb-2 text-red-600">{title}</h3>
          <p className="text-muted-foreground text-center mb-4">{error}</p>
          {onRetry && (
            <Button onClick={onRetry}>
              {retryButtonText}
            </Button>
          )}
        </CardContent>
      </Card>
    )}
    </>
  )
}
