"use client"

import { Calendar, ArrowUpDown } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { Calendar as CalendarComponent } from "@/components/ui/calendar"
import { format } from "date-fns"
import { cn } from "@/lib/utils"

export type SortOption = "date-desc" | "date-asc" | "score-desc" | "score-asc"

const sortOptions: { value: SortOption; label: string }[] = [
  { value: "date-desc", label: "Newest First" },
  { value: "date-asc", label: "Oldest First" },
  { value: "score-desc", label: "Highest Score First" },
  { value: "score-asc", label: "Lowest Score First" },
]

interface AttemptsFiltersProps {
  selectedSort: SortOption
  startDate?: Date
  endDate?: Date
  onSortChange: (sort: SortOption) => void
  onStartDateChange: (date?: Date) => void
  onEndDateChange: (date?: Date) => void
  onClearFilters: () => void
  loading: boolean
}

export function AttemptsFilters({
  selectedSort,
  startDate,
  endDate,
  onSortChange,
  onStartDateChange,
  onEndDateChange,
  onClearFilters,
  loading,
}: AttemptsFiltersProps) {
  const hasActiveFilters = selectedSort !== "date-desc" || startDate || endDate

  return (
    <Card>
      <CardContent className="pt-6">
        <div className="flex flex-col lg:flex-row gap-4">
          <div className="w-full lg:w-64 space-y-2">
            <Label htmlFor="sort" className="text-sm font-medium">
              Sort by
            </Label>
            <Select value={selectedSort} onValueChange={(value: SortOption) => onSortChange(value)} disabled={loading}>
              <SelectTrigger>
                <ArrowUpDown className="mr-2 h-4 w-4" />
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {sortOptions.map((option) => (
                  <SelectItem key={option.value} value={option.value}>
                    {option.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="flex-1 space-y-2">
            <Label>Start Date</Label>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className={cn("w-full justify-start text-left font-normal", !startDate && "text-muted-foreground")}
                  disabled={loading}
                >
                  <Calendar className="mr-2 h-4 w-4" />
                  {startDate ? format(startDate, "PPP") : "Pick a date"}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0" align="start">
                <CalendarComponent
                  mode="single"
                  selected={startDate}
                  onSelect={onStartDateChange}
                  disabled={(date) => date > new Date() || (endDate ? date > endDate : false)}
                />
              </PopoverContent>
            </Popover>
          </div>

          <div className="flex-1 space-y-2">
            <Label>End Date</Label>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className={cn("w-full justify-start text-left font-normal", !endDate && "text-muted-foreground")}
                  disabled={loading}
                >
                  <Calendar className="mr-2 h-4 w-4" />
                  {endDate ? format(endDate, "PPP") : "Pick a date"}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0" align="start">
                <CalendarComponent
                  mode="single"
                  selected={endDate}
                  onSelect={onEndDateChange}
                  disabled={(date) => date > new Date() || (!!startDate && date < startDate)}
                />
              </PopoverContent>
            </Popover>
          </div>
        </div>

        {hasActiveFilters && !loading && (
          <div className="flex items-center justify-end mt-4 pt-4 border-t">
            <Button variant="ghost" size="sm" onClick={onClearFilters}>
              Clear filters
            </Button>
          </div>
        )}
      </CardContent>
    </Card>
  )
}
