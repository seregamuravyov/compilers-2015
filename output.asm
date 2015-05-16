extern printf
extern scanf

extern strcmp
extern strcat
extern strcpy


struc firstClass
.num RESD 1
.flag RESD 1
.size:
endstruc



section .text
global main

doNothing:
push ebp
mov ebp, esp
sub esp, 0
push 1
pop eax
mov [l + firstClass.flag], eax
push 5
pop eax
mov [l + firstClass.num], eax
mov esp, ebp
pop ebp
ret



getBean:
push ebp
mov ebp, esp
sub esp, 4
push 0
pop eax
mov [t + firstClass.flag], eax
push 6
pop eax
mov [t + firstClass.num], eax
push dword [t + firstClass.flag]
push dword [globIntVar]
push 0
pop ebx
pop edx
cmp edx, ebx
je L7
mov eax, 0
jmp L8
L7:
mov eax, 1
L8:
push eax
pop ebx
pop edx
cmp edx, 1
je L10
cmp ebx, 1
je L10
mov eax, 1
jmp L11
L10:
mov eax, 0
L11:
push eax
pop eax
mov [ebp - 4], eax
push dword [ebp - 4]
mov esp, ebp
pop ebp
ret



power:
push ebp
mov ebp, esp
sub esp, 4
push 1
pop eax
mov [ebp - 4], eax
push dword [ebp + 12]
push dword [ebp + 12]
pop ebx
pop eax
imul ebx
push eax
pop eax
mov [ebp + 12], eax
push dword [ebp + 8]
push 2
pop ebx
pop eax
mov edx, 0
idiv ebx
push eax
pop eax
mov [ebp + 8], eax
push dword [ebp + 12]
push dword [ebp + 8]
pop ebx
pop eax
imul ebx
push eax
mov esp, ebp
pop ebp
ret 8



main:
push ebp
mov ebp, esp
sub esp, 8
push 5
pop eax
mov [ebp - 4], eax
push 6
pop eax
mov [ebp - 8], eax
push dword [ebp - 8]
push dword [ebp - 4]
call power
push eax
push int_format
call printf
push 0
mov esp, ebp
pop ebp
ret



section .data
int_format dd "%d", 10, 0
str_format dd "%s", 10, 0
globIntVar: dd 0
globStringVarWithInit: dd "It is global string", 0
globBoolVar: dd 0

section .bss
glob: resd 1
motherfucker: resb firstClass.size
l: resb firstClass.size
t: resb firstClass.size
