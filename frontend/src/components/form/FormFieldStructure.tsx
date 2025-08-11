import { Label } from "@/components/ui/label";

interface FormFieldProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
  id: string;
  error?: string;
  labelSuffix?: React.ReactNode;
  children?: React.ReactNode;
}

export default function FormFieldStructure({
  label,
  id,
  error,
  labelSuffix,
  children,
  ...props
}: FormFieldProps) {
  return (
    <div className="form-field">
      <div className="label-field">
        <Label htmlFor={id}>
          {label} {props.required && <span className="text-red-500">*</span>}
        </Label>
        {labelSuffix}
      </div>
      {children}
      <div className="h-4">
        {error && <p className="text-xs text-red-500">{error}</p>}
      </div>
    </div>
  );
}