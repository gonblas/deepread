import { Button } from "@/components/ui/button"

interface SectionHeaderProps {
  title: string
  loading: boolean
  error: boolean | string | null
  description?: string
  buttonText?: string
  onButtonClick?: () => void
  buttonIcon?: React.ReactNode
}

export function SectionHeader({
  title,
  loading,
  error,
  description,
  buttonText,
  onButtonClick,
  buttonIcon,
}: SectionHeaderProps) {
  return (
    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">{title}</h1>
        <p className="text-muted-foreground">
          {loading
            ? "Loading..."
            : error
              ? typeof error === "string" ? error : "Error occurred"
              : description}
        </p>
      </div>
      {buttonText && onButtonClick && (
        <Button size="lg" className="shrink-0" onClick={onButtonClick}>
          {buttonIcon && <span className="mr-2 h-5 w-5 flex items-center">{buttonIcon}</span>}
          {buttonText}
        </Button>
      )}
    </div>
  )
}
