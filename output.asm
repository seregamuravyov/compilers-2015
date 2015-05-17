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
je  L51
mov eax, 0
jmp L52
L51:
mov eax, 1
L52:
push eax
pop ebx
pop edx
cmp edx, 1
je L39
cmp ebx, 1
je L39
mov eax, 1
jmp L40
L39:
mov eax, 0
L40:
push eax
pop eax
mov [ebp - 4], eax
push dword [ebp - 4]
push int_format
call printf
add esp, 8
push dword [ebp - 4]
pop eax
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
push dword [ebp + 8]
push 0
pop ebx
pop edx
cmp edx, ebx
ja  L93
mov eax, 0
jmp L94
L93:
mov eax, 1
L94:
push eax
pop eax
cmp eax, 1
jne L87
L88:
push dword [ebp + 8]
push 2
pop ebx
pop eax
mov edx, 0
idiv ebx
mov eax, edx
push eax
push 1
pop ebx
pop edx
cmp edx, ebx
je  L109
mov eax, 0
jmp L110
L109:
mov eax, 1
L110:
push eax
pop eax
cmp eax, 1
jne L101
push dword [ebp - 4]
push dword [ebp + 12]
pop ebx
pop eax
imul ebx
push eax
pop eax
mov [ebp - 4], eax
L101:
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
push dword [ebp + 8]
push 0
pop ebx
pop edx
cmp edx, ebx
ja  L149
mov eax, 0
jmp L150
L149:
mov eax, 1
L150:
push eax
pop eax
cmp eax, 1
je L88
L87:
push dword [ebp - 4]
pop eax
mov esp, ebp
pop ebp
ret 8



main:
push ebp
mov ebp, esp
sub esp, 16
call getBean
push 2
pop eax
mov [ebp - 4], eax
push 6
pop eax
mov [ebp - 8], eax
push tmp1
push str_format
call printf
add esp, 8
lea ebx, [ebp - 8]
push ebx
push int_format
call scanf
add esp, 8
push tmp2
push str_format
call printf
add esp, 8
lea ebx, [ebp - 4]
push ebx
push int_format
call scanf
add esp, 8
push dword [ebp - 8]
push dword [ebp - 4]
call power
push eax
push int_format
call printf
add esp, 8
push tmp3
pop eax
mov [ebp - 12], eax
push tmp4
pop eax
mov [ebp - 16], eax
push dword [ebp - 12]
push dword [ebp - 16]
call strcmp
add esp, 8
mov ebx, eax
cmp ebx, 0
jne L265
mov eax, 0
jmp 266
L265:
mov eax, 1
L266:
push eax
pop eax
cmp eax, 1
jne L257
push dword [ebp - 12]
push dword [ebp - 16]
pop ebx
pop edx
push ebx
push edx
call strcat
add esp, 8
push eax
push str_format
call printf
add esp, 8
L257:
push 0
pop eax
mov esp, ebp
pop ebp
ret



section .data
int_format dd "%d", 10, 0
str_format dd "%s", 10, 0
globIntVar: dd 0
globStringVarWithInit: dd "It is global string", 0
globBoolVar: dd 0
tmp1: dd "Enter n: ", 0
tmp2: dd "Enter k: ", 0
tmp3: dd "hello, ", 0
tmp4: dd "motherfuckers!", 0

section .bss
glob: resd 1
motherfucker: resb firstClass.size
l: resb firstClass.size
t: resb firstClass.size
