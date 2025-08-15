interface SectionHeaderProps {
  title: string
  loading: boolean
  error: boolean | string | null
  description?: string
  children?: React.ReactNode
}

export function SectionHeader({
  title,
  loading,
  error,
  description,
  children,
}: SectionHeaderProps) {
  return (
    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 w-full">
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
      {children}
    </div>
  )
}
