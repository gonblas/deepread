import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import { Label } from "@/components/ui/label"

interface SelectFieldProps {
  label: string
  id: string
  value: string
  onValueChange: (value: string) => void
  options: { value: string; label: string }[]
  error?: string
  required?: boolean
  labelSuffix?: React.ReactNode
  name?: string
  ariaDescribedById?: string
}

export default function SelectField({
  label,
  id,
  value,
  onValueChange,
  options,
  error,
  required,
  labelSuffix,
  name,
  ariaDescribedById,
}: SelectFieldProps) {
  return (
    <div className="form-field">
      <div className="label-field">
        <Label htmlFor={id}>
          {label} {required && <span className="text-red-500">*</span>}
        </Label>
        {labelSuffix}
      </div>
      <Select
        id={id}
        name={name}
        value={value}
        onValueChange={onValueChange}
        aria-invalid={error ? "true" : "false"}
        aria-describedby={error ? ariaDescribedById || `${id}-error` : undefined}
        required={required}
      >
        <SelectTrigger className="w-full">
          <SelectValue placeholder={`Select a ${label.toLowerCase()}`} />
        </SelectTrigger>
        <SelectContent>
          {options.map(({ value: optValue, label: optLabel }) => (
            <SelectItem key={optValue} value={optValue}>
              {optLabel}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>
      <div className="h-4">
        {error && (
          <p
            id={ariaDescribedById || `${id}-error`}
            className="mt-1 text-sm text-red-500"
            role="alert"
          >
            {error}
          </p>
        )}
      </div>
    </div>
  )
}
