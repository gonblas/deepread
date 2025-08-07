import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

interface FormFieldProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
  id: string;
  error?: string;
  labelSuffix?: React.ReactNode;
}

export default function FormField({
  label,
  id,
  error,
  labelSuffix,
  ...props
}: FormFieldProps) {
  return (
    <div className="grid gap-3">
      <div className="flex items-center">
        <Label htmlFor={id}>{label}</Label>
        {labelSuffix}
      </div>
      <Input id={id} {...props} />
      <div className="h-4">
        {error && <p className="text-xs text-red-500">{error}</p>}
      </div>
    </div>
  );
}